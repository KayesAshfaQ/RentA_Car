package com.android.rentacar.wedding_package;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.rentacar.utils.Constant;
import com.android.rentacar.R;
import com.android.rentacar.remote.ApiClient;
import com.android.rentacar.remote.ApiInterface;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WeddingPackDetailsActivity extends AppCompatActivity {

    String id, name_str, price_str, details_str, image_str, getCell;
    ImageView expandedImage;
    FloatingActionButton floatingActionButton;
    TextView pack_name, price, details;
    private ApiInterface apiInterface;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wedding_pack_details);

        //fetching data form calling Activity
        id = getIntent().getStringExtra("package_id");
        name_str = getIntent().getStringExtra("package_name");
        price_str = getIntent().getStringExtra("package_price");
        details_str = getIntent().getStringExtra("details");
        image_str = getIntent().getStringExtra("image");

        //adding toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(name_str);
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        //Fetching cell from shared preferences
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        getCell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");

        //adding views from design
        floatingActionButton = findViewById(R.id.fab);
        pack_name = findViewById(R.id.pack_name);
        price = findViewById(R.id.price);
        details = findViewById(R.id.info);
        expandedImage = findViewById(R.id.expandedImage);

        //adding data on views
        pack_name.setText(name_str);
        price.setText(price_str + " BDT");
        details.setText(details_str);
        Glide.with(WeddingPackDetailsActivity.this).load(Constant.WEDDING_PACK_IMAGE_URL + image_str).into(expandedImage);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeddingPackDetailsActivity.this, WeddingPackOrder.class);
                intent.putExtra("package_id", name_str);
                intent.putExtra("package_name", name_str);
                intent.putExtra("package_price", price_str);
                intent.putExtra("image", image_str);
                startActivity(intent);
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