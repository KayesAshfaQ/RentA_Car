package com.android.rentacar.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.rentacar.ConnectionDetector;
import com.android.rentacar.Constant;
import com.android.rentacar.MainActivity;
import com.android.rentacar.R;
import com.android.rentacar.adapter.CartAdapter;
import com.android.rentacar.model.Cart;
import com.android.rentacar.remote.ApiClient;
import com.android.rentacar.remote.ApiInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    String getCell;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CartAdapter adapter;
    private List<Cart> cartList;
    private ApiInterface apiInterface;
    private ProgressBar progressBar;
    private Button next_btn;
    private ArrayList<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Product Cart");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        next_btn = findViewById(R.id.next_btn);

        items = new ArrayList<>();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        //Fetching cell from shared preferences
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getCell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");

        //Internet connection checker
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            Toast.makeText(CartActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
        } else {
            getCartItem(getCell);
        }


        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = 0;
                if (cartList != null) {
                    Intent intent = new Intent(CartActivity.this, OrderActivity.class);

                    startActivity(intent);
                }
            }
        });

    }

    public void getCartItem(String cell) {
        Call<List<Cart>> call = apiInterface.getCartItem(cell);
        call.enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                progressBar.setVisibility(View.INVISIBLE);
                cartList = response.body();
                adapter = new CartAdapter(CartActivity.this, cartList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();//for search
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(CartActivity.this, "Error : " + t.toString(), Toast.LENGTH_SHORT).show();
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