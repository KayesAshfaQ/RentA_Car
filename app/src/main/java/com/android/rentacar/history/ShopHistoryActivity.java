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
import com.android.rentacar.adapter.ShopHistoryMainAdapter;
import com.android.rentacar.model.ShopOrderUser;
import com.android.rentacar.remote.ApiClient;
import com.android.rentacar.remote.ApiInterface;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ShopHistoryMainAdapter adapter;
    private List<ShopOrderUser> orders;
    private ApiInterface apiInterface;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_history);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Shopping Histories");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        recyclerView = findViewById(R.id.recyclerView);
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
            Toasty.error(ShopHistoryActivity.this, "No Internet Connection", Toasty.LENGTH_LONG).show();
        } else {
            getHistoryOrderId(phone_number);
            LinearLayoutManager manager = new LinearLayoutManager(ShopHistoryActivity.this);
            recyclerView.setLayoutManager(manager);

        }


    }

    private void getHistoryOrderId(String phone_number) {

        loading = new ProgressDialog(this);
        loading.setMessage("loading...");
        loading.show();

        Call<List<ShopOrderUser>> call = apiInterface.getOrderId(phone_number);
        call.enqueue(new Callback<List<ShopOrderUser>>() {
            @Override
            public void onResponse(Call<List<ShopOrderUser>> call, Response<List<ShopOrderUser>> response) {
                if (response.isSuccessful()) {
                    loading.dismiss();
                    orders = response.body();

                    adapter = new ShopHistoryMainAdapter(ShopHistoryActivity.this, orders);
                    recyclerView.setAdapter(adapter);
                    //adapter.notifyDataSetChanged();
                } else {
                    loading.dismiss();
                    Toasty.error(ShopHistoryActivity.this, "response is unsuccessful", Toasty.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ShopOrderUser>> call, Throwable t) {
                loading.dismiss();
                Toasty.error(ShopHistoryActivity.this, "error", Toasty.LENGTH_LONG).show();
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