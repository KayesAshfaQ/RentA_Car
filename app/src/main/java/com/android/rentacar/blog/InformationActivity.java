package com.android.rentacar.blog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.android.rentacar.utils.ConnectionDetector;
import com.android.rentacar.R;
import com.android.rentacar.adapter.InformationAdapter;
import com.android.rentacar.model.Information;
import com.android.rentacar.remote.ApiClient;
import com.android.rentacar.remote.ApiInterface;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InformationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    InformationAdapter adapter;
    private List<Information> informationList;
    private ApiInterface apiInterface;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Information");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        //Internet connection checker
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            Toasty.error(InformationActivity.this, "No Internet Connection", Toasty.LENGTH_LONG).show();
        } else {
            getInfromtion();
            LinearLayoutManager manager = new LinearLayoutManager(InformationActivity.this);
            recyclerView.setLayoutManager(manager);

        }

    }

    private void getInfromtion() {

        pd = new ProgressDialog(InformationActivity.this);
        pd.setMessage("loading...");
        pd.show();

        Call<List<Information>> call = apiInterface.getInformation();
        call.enqueue(new Callback<List<Information>>() {
            @Override
            public void onResponse(Call<List<Information>> call, Response<List<Information>> response) {

                if (response.isSuccessful()){
                    pd.dismiss();
                    informationList = response.body();
                    adapter = new InformationAdapter(InformationActivity.this, informationList);
                    recyclerView.setAdapter(adapter);
                }else {
                    pd.dismiss();
                    Toasty.error(InformationActivity.this, "try again!", Toasty.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<Information>> call, Throwable t) {
                pd.dismiss();
                Toasty.error(InformationActivity.this, "errror!", Toasty.LENGTH_LONG).show();
            }
        });

    }

    //for back button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}