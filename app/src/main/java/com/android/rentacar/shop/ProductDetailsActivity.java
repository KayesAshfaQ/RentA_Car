package com.android.rentacar.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.rentacar.Constant;
import com.android.rentacar.R;
import com.android.rentacar.model.Cart;
import com.android.rentacar.remote.ApiClient;
import com.android.rentacar.remote.ApiInterface;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProductDetailsActivity extends AppCompatActivity {

    String productName, productPrice, productDetails, productImage, getCell;
    ImageView productIv;
    FloatingActionButton floatingActionButton;
    TextView nameTv, priceTv, detailsTv, number;
    Button add, minus;
    private ApiInterface apiInterface;
    private ProgressDialog loading;
    int numer_of_amount =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productName = getIntent().getStringExtra("name");
        productDetails = getIntent().getStringExtra("details");
        productPrice = getIntent().getStringExtra("price");
        productImage = getIntent().getStringExtra("image");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(productName);
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        //Fetching cell from shared preferences
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getCell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");
        floatingActionButton = findViewById(R.id.fab);
        nameTv = findViewById(R.id.name_tv);
        priceTv = findViewById(R.id.price_tv);
        detailsTv = findViewById(R.id.details_tv);
        productIv = findViewById(R.id.expandedImage);
        add = findViewById(R.id.add);
        minus = findViewById(R.id.minus);
        number = findViewById(R.id.number);

        numer_of_amount = Integer.parseInt(number.getText().toString());
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numer_of_amount < 25) {
                    numer_of_amount++;
                    number.setText(String.valueOf(numer_of_amount));
                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numer_of_amount > 1) {
                    numer_of_amount--;
                    number.setText(String.valueOf(numer_of_amount));
                }

            }
        });

        nameTv.setText(productName);
        priceTv.setText("Tk " + productPrice);
        detailsTv.setText(productDetails);
        Glide.with(ProductDetailsActivity.this).load(Constant.IMAGE_URL + productImage).into(productIv);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart(productName, productPrice, productImage, String.valueOf(numer_of_amount), getCell);
            }
        });
    }

    private void addToCart(String name, String price, String image, String amount, String cell) {

        loading = new ProgressDialog(this);
        loading.setMessage("Please wait....");
        loading.show();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Cart> call = apiInterface.addToCart(name, price, image, amount, cell);
        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {

                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("success")) {
                    loading.dismiss();
                    Toasty.success(ProductDetailsActivity.this, message, Toasty.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProductDetailsActivity.this, CartActivity.class);
                    startActivity(intent);
                } else {
                    loading.dismiss();
                    Toasty.error(ProductDetailsActivity.this, message, Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {

                loading.dismiss();
                Toasty.error(ProductDetailsActivity.this, "Error! " + t.toString(), Toasty.LENGTH_SHORT).show();
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



