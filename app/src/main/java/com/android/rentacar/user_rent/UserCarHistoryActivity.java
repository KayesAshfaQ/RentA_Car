package com.android.rentacar.user_rent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.rentacar.R;
import com.android.rentacar.adapter.ShopHistoryMainAdapter;
import com.android.rentacar.adapter.UserCarAdapter;
import com.android.rentacar.history.ShopHistoryActivity;
import com.android.rentacar.model.RentUserCar;
import com.android.rentacar.model.ShopOrderUser;
import com.android.rentacar.remote.ApiClient;
import com.android.rentacar.remote.ApiInterface;
import com.android.rentacar.utils.ConnectionDetector;
import com.android.rentacar.utils.Constant;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCarHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserCarAdapter adapter;
    private List<RentUserCar> cars;
    private ApiInterface apiInterface;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_car_history);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Shopping Histories");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        //Fetching cell from shared preferences
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String phone_number = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");

        //Internet connection checker
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            Toasty.error(UserCarHistoryActivity.this, "No Internet Connection", Toasty.LENGTH_LONG).show();
        } else {
            getUserCars(phone_number);
            LinearLayoutManager manager = new LinearLayoutManager(UserCarHistoryActivity.this);
            recyclerView.setLayoutManager(manager);

        }


    }

    private void getUserCars(String phone_number) {

        loading = new ProgressDialog(this);
        loading.setMessage("loading...");
        loading.show();

        Call<List<RentUserCar>> call = apiInterface.getUserCar(phone_number);
        call.enqueue(new Callback<List<RentUserCar>>() {
            @Override
            public void onResponse(Call<List<RentUserCar>> call, Response<List<RentUserCar>> response) {
                if (response.isSuccessful()) {
                    loading.dismiss();
                    cars = response.body();

                    Log.d("TAG_User_His", String.valueOf(cars.size()));

                    adapter = new UserCarAdapter(UserCarHistoryActivity.this, cars);
                    recyclerView.setAdapter(adapter);
                    //adapter.notifyDataSetChanged();
                } else {
                    loading.dismiss();
                    Toasty.error(UserCarHistoryActivity.this, "response is unsuccessful", Toasty.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<RentUserCar>> call, Throwable t) {

                loading.dismiss();
                Toasty.error(UserCarHistoryActivity.this, "response is unsuccessful", Toasty.LENGTH_LONG).show();

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