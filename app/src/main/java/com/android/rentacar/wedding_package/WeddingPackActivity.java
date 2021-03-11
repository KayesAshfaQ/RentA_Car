package com.android.rentacar.wedding_package;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.rentacar.utils.ConnectionDetector;
import com.android.rentacar.history.HistoryWeddingPackActivity;
import com.android.rentacar.R;
import com.android.rentacar.adapter.WeddingPackAdapter;
import com.android.rentacar.model.WeddingPack;
import com.android.rentacar.remote.ApiClient;
import com.android.rentacar.remote.ApiInterface;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeddingPackActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    WeddingPackAdapter adapter;
    private List<WeddingPack> weddingPackList;
    private ApiInterface apiInterface;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wedding_pack);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Wedding Packages");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        //Internet connection checker
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            Toasty.error(WeddingPackActivity.this, "No Internet Connection", Toasty.LENGTH_LONG).show();
        } else {
            getWeddingPackageData();
            LinearLayoutManager manager = new LinearLayoutManager(WeddingPackActivity.this);
            recyclerView.setLayoutManager(manager);

        }

    }

    private void getWeddingPackageData() {

        pd = new ProgressDialog(this);
        pd.setMessage("loading...");
        pd.show();

        Call<List<WeddingPack>> call = apiInterface.getWeddingPack();
        call.enqueue(new Callback<List<WeddingPack>>() {
            @Override
            public void onResponse(Call<List<WeddingPack>> call, Response<List<WeddingPack>> response) {
                if (response.isSuccessful()) {
                    pd.dismiss();
                    weddingPackList = response.body();
                    adapter = new WeddingPackAdapter(WeddingPackActivity.this, weddingPackList);
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<List<WeddingPack>> call, Throwable t) {
                pd.dismiss();
                Toasty.error(WeddingPackActivity.this, "errror!", Toasty.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.wedding_pack_history, menu);
        return super.onCreateOptionsMenu(menu);

    }

    //for back button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;*/
            case R.id.history_wedding:
                startActivity(new Intent(WeddingPackActivity.this, HistoryWeddingPackActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}