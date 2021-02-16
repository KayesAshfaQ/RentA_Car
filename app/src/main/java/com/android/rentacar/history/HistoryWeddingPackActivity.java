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
import com.android.rentacar.adapter.PackageHistoryAdapter;
import com.android.rentacar.model.OrderWeddingPack;
import com.android.rentacar.remote.ApiClient;
import com.android.rentacar.remote.ApiInterface;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryWeddingPackActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PackageHistoryAdapter adapter;
    List<OrderWeddingPack> orderedPackageList;
    private ApiInterface apiInterface;
    ProgressDialog loading;
    String phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_wedding_pack);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Wedding pack Histories");
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
            Toasty.error(HistoryWeddingPackActivity.this, "No Internet Connection", Toasty.LENGTH_LONG).show();
        } else {
            getHistoryData();
            LinearLayoutManager manager = new LinearLayoutManager(HistoryWeddingPackActivity.this);
            recyclerView.setLayoutManager(manager);

        }

    }

    private void getHistoryData() {

        loading = new ProgressDialog(this);
        loading.setMessage("loading...");
        loading.show();

        Call<List<OrderWeddingPack>> call = apiInterface.getorderPack(phone_number);

        call.enqueue(new Callback<List<OrderWeddingPack>>() {
            @Override
            public void onResponse(Call<List<OrderWeddingPack>> call, Response<List<OrderWeddingPack>> response) {

                if (response.isSuccessful()){
                    loading.dismiss();
                    orderedPackageList = response.body();
                    adapter = new PackageHistoryAdapter(HistoryWeddingPackActivity.this, orderedPackageList);
                    recyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onFailure(Call<List<OrderWeddingPack>> call, Throwable t) {
                loading.dismiss();
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