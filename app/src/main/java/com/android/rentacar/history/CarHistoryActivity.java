package com.android.rentacar.history;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.rentacar.ConnectionDetector;
import com.android.rentacar.Constant;
import com.android.rentacar.R;
import com.android.rentacar.adapter.HistoryAdapter;
import com.android.rentacar.model.CarBook;
import com.android.rentacar.remote.ApiClient;
import com.android.rentacar.remote.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<CarBook> bookedCarList;
    private ApiInterface apiInterface;
    private ProgressDialog loading;
    private String phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_history);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Car Book Histories");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        //Fetching cell from shared preferences
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        phone_number = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");

        //Internet connection checker
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            Toasty.error(CarHistoryActivity.this, "No Internet Connection", Toasty.LENGTH_LONG).show();
        } else {
            getHistoryData();
            LinearLayoutManager manager = new LinearLayoutManager(CarHistoryActivity.this);
            recyclerView.setLayoutManager(manager);

        }
    }

    private void getHistoryData() {

        loading = new ProgressDialog(this);
        loading.setMessage("loading...");
        loading.show();

        Call<List<CarBook>> call = apiInterface.getCarBook();
        call.enqueue(new Callback<List<CarBook>>() {
            @Override
            public void onResponse(Call<List<CarBook>> call, Response<List<CarBook>> response) {

                loading.dismiss();
                //bookedCarList.clear();
                bookedCarList = response.body();

                List<CarBook> bookedCarListCheckedPhone = new ArrayList<>();

                for (int i = 0; i < bookedCarList.size(); i++) {
                    if (bookedCarList.get(i).getMobile_no().equals(phone_number)) {
                        bookedCarListCheckedPhone.add(bookedCarList.get(i));
                    }
                }

                adapter = new HistoryAdapter(CarHistoryActivity.this, bookedCarListCheckedPhone);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<CarBook>> call, Throwable t) {
                loading.dismiss();
                Toasty.error(CarHistoryActivity.this, "error", Toasty.LENGTH_LONG).show();
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