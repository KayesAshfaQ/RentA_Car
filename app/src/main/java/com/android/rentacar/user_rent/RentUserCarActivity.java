package com.android.rentacar.user_rent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.rentacar.MainActivity;
import com.android.rentacar.R;
import com.android.rentacar.car_order.CarBookActivity;
import com.android.rentacar.model.Cart;
import com.android.rentacar.model.RentUserCar;
import com.android.rentacar.remote.ApiClient;
import com.android.rentacar.remote.ApiInterface;
import com.android.rentacar.utils.ConnectionDetector;
import com.android.rentacar.utils.Constant;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RentUserCarActivity extends AppCompatActivity {

    private EditText model, brand, car_number, seat_no, fare, driver_name, mobile_no;
    private String modelStr, brandStr, numberStr, seatStr, fareStr, driverNameStr, mobile;
    private ApiInterface apiInterface;
    private ProgressDialog loading;
    private Button confirm_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_user_car);

        //adding toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rent Your Car");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        //Fetching cell from shared preferences
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        mobile = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");

        //adding views
        model = findViewById(R.id.model);
        brand = findViewById(R.id.brand);
        car_number = findViewById(R.id.car_number);
        seat_no = findViewById(R.id.seat_no);
        fare = findViewById(R.id.fare);
        driver_name = findViewById(R.id.driver_name);
        mobile_no = findViewById(R.id.mobile_no);
        confirm_btn = findViewById(R.id.confirm_btn);

        //set auto mobile number
        if (!mobile.isEmpty()) {
            mobile_no.setText(mobile);
        }

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Internet connection checker
                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                // Check if Internet present
                if (!cd.isConnectingToInternet()) {
                    // Internet Connection is not present
                    Toasty.error(RentUserCarActivity.this, "No Internet Connection", Toasty.LENGTH_LONG).show();
                } else {
                    modelStr = model.getText().toString();
                    brandStr = brand.getText().toString();
                    numberStr = car_number.getText().toString();
                    seatStr = seat_no.getText().toString();
                    fareStr = fare.getText().toString();
                    driverNameStr = driver_name.getText().toString();

                    //validation
                    if (mobile.isEmpty() || mobile.length() != 11 || !mobile.startsWith("01")) {
                        mobile_no.setError("Invalid Mobile Number!");
                        mobile_no.requestFocus();

                    } else if (modelStr.isEmpty()) {
                        model.setError("this field can't be empty");
                        model.requestFocus();

                    } else if (seatStr.isEmpty()) {
                        seat_no.setError("this field can't be empty");
                        seat_no.requestFocus();

                    }else if (fareStr.isEmpty()) {
                        fare.setError("this field can't be empty");
                        fare.requestFocus();

                    }else if (numberStr.isEmpty()) {
                        car_number.setError("this field can't be empty");
                        car_number.requestFocus();

                    }else if (driverNameStr.isEmpty()) {
                        driver_name.setError("this field can't be empty");
                        driver_name.requestFocus();

                    }else {

                        //add car to server
                        addCar();

                    }
                }
            }
        });


    }

    private void addCar() {

        loading = new ProgressDialog(this);
        loading.setMessage("Loading...");
        loading.show();

        Call<RentUserCar> call = apiInterface.addUserCar(modelStr, brandStr, numberStr, seatStr, fareStr, driverNameStr, mobile, "2");
        call.enqueue(new Callback<RentUserCar>() {
            @Override
            public void onResponse(Call<RentUserCar> call, Response<RentUserCar> response) {

                String value = response.body().getValue();

                if (value.equals("success")) {
                    loading.dismiss();
                    Toasty.success(RentUserCarActivity.this, "successfully car booked ", Toasty.LENGTH_SHORT).show();
                    Intent intent = new Intent(RentUserCarActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();


                } else {
                    loading.dismiss();
                    Toasty.error(RentUserCarActivity.this, "try again", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RentUserCar> call, Throwable t) {
                loading.dismiss();
                Toasty.error(RentUserCarActivity.this, "Error! " + t.toString(), Toasty.LENGTH_SHORT).show();
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