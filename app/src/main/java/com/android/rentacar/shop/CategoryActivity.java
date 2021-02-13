package com.android.rentacar.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.rentacar.ConnectionDetector;
import com.android.rentacar.R;
import com.android.rentacar.adapter.CategoryAdapter;
import com.android.rentacar.model.Category;
import com.android.rentacar.remote.ApiClient;
import com.android.rentacar.remote.ApiInterface;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CategoryAdapter adapter;
    private List<Category> categoryList;
    private ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Choose Category");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        //Internet connection checker
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            Toasty.error(CategoryActivity.this, "No Internet Connection", Toasty.LENGTH_LONG).show();
        }else {
            getCategoryData("","");}
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
    }


    public void getCategoryData(String name, String image) {
        Call<List<Category>> call = apiInterface.getCategory(name,image);
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                categoryList = response.body();
                adapter = new CategoryAdapter(CategoryActivity.this, categoryList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(CategoryActivity.this, "Error : "+ t.toString(), Toast.LENGTH_SHORT).show();
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

