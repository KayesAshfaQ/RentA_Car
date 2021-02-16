package com.android.rentacar.car_order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.rentacar.ConnectionDetector;
import com.android.rentacar.R;
import com.android.rentacar.adapter.WeddingCarAdapter;
import com.android.rentacar.model.Car;
import com.android.rentacar.remote.ApiClient;
import com.android.rentacar.remote.ApiInterface;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeddingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    WeddingCarAdapter adapter;
    private List<Car> carsList;
    private ApiInterface apiInterface;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wedding);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Wedding Cars");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        recyclerView = findViewById(R.id.recyclerView);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        //Internet connection checker
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            Toasty.error(WeddingActivity.this, "No Internet Connection", Toasty.LENGTH_LONG).show();
        } else {
            getCarsData();
            LinearLayoutManager manager = new LinearLayoutManager(WeddingActivity.this);
            recyclerView.setLayoutManager(manager);

        }

    }

    private void getCarsData() {

        pd = new ProgressDialog(this);
        pd.setMessage("loading...");
        pd.show();

        Call<List<Car>> call = apiInterface.getCar();
        call.enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.isSuccessful()) {
                    pd.dismiss();
                    carsList = response.body();
                    adapter = new WeddingCarAdapter(WeddingActivity.this, carsList);
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                pd.dismiss();
                Toasty.error(WeddingActivity.this, "errror!", Toasty.LENGTH_LONG).show();
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