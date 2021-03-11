package com.android.rentacar.car_order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.rentacar.utils.Constant;
import com.android.rentacar.R;
import com.android.rentacar.remote.ApiClient;
import com.android.rentacar.remote.ApiInterface;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CarDetailsActivity extends AppCompatActivity {

    String id, name, price, seatNumber, driverName, carNumber, details, carImage, getCell;
    ImageView productIv;
    FloatingActionButton floatingActionButton;
    TextView car_name, fare, seat_no, driver_name,car_number,details_tv;
    private ApiInterface apiInterface;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        //fetching data form calling Activity
        id = getIntent().getStringExtra("vehicle_id");
        name = getIntent().getStringExtra("vehicle_name");
        price = getIntent().getStringExtra("vehicle_fare");
        seatNumber = getIntent().getStringExtra("seat_no");
        driverName = getIntent().getStringExtra("driver_name");
        carNumber = getIntent().getStringExtra("vehicle_no");
        details = getIntent().getStringExtra("details");
        carImage = getIntent().getStringExtra("image");

        //adding toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        //Fetching cell from shared preferences
        SharedPreferences sharedPreferences;
        sharedPreferences =getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getCell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");

        //adding views from design
        floatingActionButton=findViewById(R.id.fab);
        car_name=findViewById(R.id.car_name);
        fare=findViewById(R.id.fare);
        seat_no=findViewById(R.id.seat_no);
        driver_name=findViewById(R.id.driver_name);
        car_number=findViewById(R.id.car_number);
        details_tv=findViewById(R.id.details_tv);
        productIv=findViewById(R.id.expandedImage);

        //adding data on views
        car_name.setText(name);
        fare.setText("Tk "+price+"per hour");
        seat_no.setText(seatNumber);
        driver_name.setText(driverName);
        car_number.setText(carNumber);
        details_tv.setText(details);
        Glide.with(CarDetailsActivity.this).load(Constant.CAR_IMAGE_URL+carImage).into(productIv);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CarDetailsActivity.this, CarBookActivity.class);
                i.putExtra("vehicle_id", id);
                i.putExtra("vehicle_name", name);
                i.putExtra("vehicle_fare", price);
                i.putExtra("image",carImage);
                i.putExtra("vehicle_no", carNumber);
                startActivity(i);
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