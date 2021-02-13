package com.android.rentacar.wedding_package;

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
import com.android.rentacar.model.OrderWeddingPack;
import com.android.rentacar.remote.ApiClient;
import com.android.rentacar.remote.ApiInterface;
import com.bumptech.glide.Glide;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeddingPackOrder extends AppCompatActivity {

    String id, name_str, price_str, image_str, getCell;
    ImageView expandedImage;
    Button confirm_book;
    TextView pack_name, price;
    EditText address, mobile_no, pack_time, pack_date, description;
    String address_str, mobile_str, time_str, date_str, description_str;
    private ApiInterface apiInterface;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wedding_pack_order);

        //fetching data form calling Activity
        id = getIntent().getStringExtra("package_id");
        name_str = getIntent().getStringExtra("package_name");
        price_str = getIntent().getStringExtra("package_price");
        image_str = getIntent().getStringExtra("image");

        //adding toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name_str);
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        //Fetching cell from shared preferences
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getCell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");

        //adding views from design
        expandedImage = findViewById(R.id.expandedImage);
        confirm_book = findViewById(R.id.confirm_book);
        pack_name = findViewById(R.id.pack_name);
        price = findViewById(R.id.price);
        address = findViewById(R.id.address);
        mobile_no = findViewById(R.id.mobile_no);
        pack_time = findViewById(R.id.pack_time);
        pack_date = findViewById(R.id.pack_date);
        description = findViewById(R.id.description);

        //set_value
        Glide.with(WeddingPackOrder.this).load(Constant.WEDDING_PACK_IMAGE_URL + image_str).into(expandedImage);
        pack_name.setText(name_str);
        price.setText(price_str + " BDT");

        if (!getCell.isEmpty()) {
            mobile_no.setText(getCell);
        }
        //setTimePicker();
        pack_time.setFocusable(false);
        pack_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(WeddingPackOrder.this, new TimePickerDialog.OnTimeSetListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {

                        String am_pm = (timePicker.getCurrentHour() < 12) ? "AM" : "PM";
                        if (hour > 12) {
                            hour = hour - 12;
                        } else if (hour == 0) {
                            hour = 12;
                        }
                        pack_time.setText(hour + " : " + min + " " + am_pm);

                    }
                }, 10, 0, false);
                timePickerDialog.show();
            }
        });
        //setDatePicker();

        pack_date.setFocusable(false);
        pack_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(

                        WeddingPackOrder.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                pack_date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

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
                    Toasty.error(WeddingPackOrder.this, "No Internet Connection", Toasty.LENGTH_LONG).show();
                } else {
                    address_str = address.getText().toString();
                    mobile_str = mobile_no.getText().toString();
                    time_str = pack_time.getText().toString();
                    date_str = pack_date.getText().toString();
                    description_str = description.getText().toString();

                    //getCall

                    //validation
                    if (getCell.isEmpty() || getCell.length() != 11 || !getCell.startsWith("01")) {
                        mobile_no.setError("Invalid cell!");
                        mobile_no.requestFocus();

                    } else if (address_str.isEmpty()) {
                        address.setError("address can not be empty! ");
                        address.requestFocus();

                    } else if (mobile_str.isEmpty()) {
                        mobile_no.setError("mobile no can not be empty! ");
                        mobile_no.requestFocus();

                    } else if (time_str.isEmpty()) {
                        pack_time.setError("please select a time! ");
                        pack_time.requestFocus();

                    } else if (date_str.isEmpty()) {
                        pack_date.setError("please select a date! ");
                        pack_date.requestFocus();

                    } else {

                        orderPackage();

                    }

                }

            }
        });

    }

    private void orderPackage() {
        loading = new ProgressDialog(this);
        loading.setMessage("Loading...");
        loading.show();
        Call<OrderWeddingPack> call = apiInterface.orderPack(name_str, price_str, address_str, mobile_str,
                time_str, date_str, description_str, "0");

        call.enqueue(new Callback<OrderWeddingPack>() {

            @Override
            public void onResponse(Call<OrderWeddingPack> call, Response<OrderWeddingPack> response) {
                String value = response.body().getValue();
                if (value.equals("success")) {
                    loading.dismiss();
                    Toasty.success(WeddingPackOrder.this, " package ordered successfully ", Toasty.LENGTH_SHORT).show();
                    Intent intent = new Intent(WeddingPackOrder.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    loading.dismiss();
                    Toasty.error(WeddingPackOrder.this, "try again", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderWeddingPack> call, Throwable t) {
                loading.dismiss();
                Toasty.error(WeddingPackOrder.this, "Error! " + t.toString(), Toasty.LENGTH_SHORT).show();
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

}