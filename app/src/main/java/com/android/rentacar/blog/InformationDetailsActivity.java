package com.android.rentacar.blog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.rentacar.utils.Constant;
import com.android.rentacar.R;
import com.bumptech.glide.Glide;

public class InformationDetailsActivity extends AppCompatActivity {

    ImageView imageView;
    TextView titleView, dateView, timeView, detailsView, linkView;
    String image, title, date, time, details, link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Blog");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button

        //find views
        imageView = findViewById(R.id.blog_image);
        titleView = findViewById(R.id.title);
        dateView = findViewById(R.id.driver_name);
        timeView = findViewById(R.id.car_number);
        detailsView = findViewById(R.id.details);
        linkView = findViewById(R.id.link);

        //fetch data
        image = getIntent().getStringExtra("image");
        title = getIntent().getStringExtra("title");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        details = getIntent().getStringExtra("details");
        link = getIntent().getStringExtra("link");

        //set data in views
        Glide.with(imageView.getContext()).load(Constant.BLOG_IMAGE_URL +image).into(imageView);
        titleView.setText(title);
        detailsView.setText(details);
        timeView.setText("Time: "+time);
        dateView.setText("Date: "+date);

        linkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
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