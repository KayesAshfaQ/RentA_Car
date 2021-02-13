package com.android.rentacar.car_order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.rentacar.ConnectionDetector;
import com.android.rentacar.Constant;
import com.android.rentacar.MainActivity;
import com.android.rentacar.R;
import com.android.rentacar.model.CarBook;
import com.android.rentacar.remote.ApiClient;
import com.android.rentacar.remote.ApiInterface;
import com.bumptech.glide.Glide;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarBookActivity extends AppCompatActivity {

    String currentTime;
    String pickup_edt, destination_edt, passenger_edt, date_edt, time_edt;
    String name, price, carImage, getCell, vehicle_no;
    ImageView expandedImage;
    Button confirm_book;
    TextView car_name, fare;
    EditText pickup, destination, mobile_no, passenger_no, type, trip_time, trip_date;
    private ApiInterface apiInterface;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_book);

        //fetching data form calling Activity
        name = getIntent().getStringExtra("vehicle_name");
        price = getIntent().getStringExtra("vehicle_fare");
        carImage = getIntent().getStringExtra("image");
        vehicle_no = getIntent().getStringExtra("vehicle_no");

        //adding toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        //Fetching cell from shared preferences
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getCell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");

        //adding views from design
        expandedImage = findViewById(R.id.expandedImage);
        car_name = findViewById(R.id.car_name);
        fare = findViewById(R.id.fare);
        pickup = findViewById(R.id.pickup);
        destination = findViewById(R.id.destination);
        mobile_no = findViewById(R.id.mobile_no);
        passenger_no = findViewById(R.id.passenger_no);
        trip_time = findViewById(R.id.trip_time);
        trip_date = findViewById(R.id.trip_date);
        confirm_book = findViewById(R.id.confirm_book);
        type = findViewById(R.id.type);


        //set_value
        Glide.with(CarBookActivity.this).load(Constant.CAR_IMAGE_URL + carImage).into(expandedImage);
        car_name.setText(name);
        fare.setText("Tk " + price + "per hour");

        if (!getCell.isEmpty()) {
            mobile_no.setText(getCell);
        }
        //setTimePicker();
        trip_time.setFocusable(false);
        trip_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(CarBookActivity.this, new TimePickerDialog.OnTimeSetListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {

                        String am_pm = (timePicker.getCurrentHour() < 12) ? "AM" : "PM";
                        if (hour>12){
                            hour=hour-12;
                        }else if (hour==0){
                            hour=12;
                        }
                        trip_time.setText(hour + " : " + min+" "+ am_pm);

                    }
                }, 10, 0, false);
                timePickerDialog.show();
            }
        });
        //setDatePicker();

        trip_date.setFocusable(false);
        trip_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(

                        CarBookActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                trip_date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                            }
                        }, 2021, 1, 1
                );
                datePickerDialog.show();
            }
        });


        confirm_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Internet connection checker
                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                // Check if Internet present
                if (!cd.isConnectingToInternet()) {
                    // Internet Connection is not present
                    Toasty.error(CarBookActivity.this, "No Internet Connection", Toasty.LENGTH_LONG).show();
                } else {
                    pickup_edt = pickup.getText().toString();
                    destination_edt = destination.getText().toString();
                    passenger_edt = passenger_no.getText().toString();
                    date_edt = trip_date.getText().toString();
                    time_edt = trip_time.getText().toString();
                    //getCall

                    //validation
                    if (getCell.isEmpty() || getCell.length() != 11 || !getCell.startsWith("01")) {
                        mobile_no.setError("Invalid Mobile Number!");
                        mobile_no.requestFocus();

                    } else if (pickup_edt.isEmpty()) {
                        pickup.setError("please enter Pick-Up Address ");
                        pickup.requestFocus();

                    } else if (destination_edt.isEmpty()) {
                        destination.setError("please enter destination Address ");
                        destination.requestFocus();

                    } else if (passenger_edt.isEmpty()) {
                        passenger_no.setError("please enter number on passengers.");
                        passenger_no.requestFocus();

                    } else if (time_edt.isEmpty()) {
                        trip_time.setError("You must select a time");
                        trip_time.requestFocus();

                    } else if (date_edt.isEmpty()) {
                        trip_date.setError("You must select a date");
                        trip_date.requestFocus();

                    } else {

                        bookCar();

                    }

                }

            }
        });

    }

    private void bookCar() {
        loading = new ProgressDialog(this);
        loading.setMessage("Loading...");
        loading.show();

        Call<CarBook> call = apiInterface.bookCar(name, price, vehicle_no, pickup_edt, destination_edt, getCell, passenger_edt, "0", time_edt, date_edt, "0");

        call.enqueue(new Callback<CarBook>() {
            @Override
            public void onResponse(Call<CarBook> call, Response<CarBook> response) {

                String value = response.body().getValue();

                if (value.equals("success")) {
                    loading.dismiss();
                    Toasty.success(CarBookActivity.this, "successfully car booked ", Toasty.LENGTH_SHORT).show();
                    Intent intent = new Intent(CarBookActivity.this, MainActivity.class);
                    startActivity(intent);


                } else {
                    loading.dismiss();
                    Toasty.error(CarBookActivity.this, "try again", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CarBook> call, Throwable t) {

                loading.dismiss();
                Toasty.error(CarBookActivity.this, "Error! " + t.toString(), Toasty.LENGTH_SHORT).show();
            }
        });

    }

    //for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //time set
    private void setTimePicker() {

        TimePicker timePicker = new TimePicker(getApplicationContext());
        timePicker.setIs24HourView(false);
        final int min = timePicker.getCurrentMinute();
        final int hour = timePicker.getCurrentHour();
        currentTime = hour + " : " + min;
        trip_time.setText(currentTime);

    }

    //date set
    private void setDatePicker() {
        DatePicker datePicker = new DatePicker(getApplicationContext());

        final int date = datePicker.getDayOfMonth();
        final int month = datePicker.getMonth() + 1;
        final int year = datePicker.getYear();

        String string = date + "/" + month + "/" + year;
        trip_date.setText(string);
    }

}