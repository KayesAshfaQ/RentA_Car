package com.android.rentacar.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.rentacar.utils.ConnectionDetector;
import com.android.rentacar.utils.Constant;
import com.android.rentacar.R;
import com.android.rentacar.model.UserProfile;
import com.android.rentacar.remote.ApiClient;
import com.android.rentacar.remote.ApiInterface;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    ProgressDialog pd;
    ApiInterface apiInterface;
    private TextView profile_name, profile_cell, profile_address, profile_Gender;
    private String cell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //adding toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button

        //find views
        profile_name = findViewById(R.id.profile_name);
        profile_cell = findViewById(R.id.profile_cell);
        profile_address = findViewById(R.id.profile_address);
        profile_Gender = findViewById(R.id.profile_Gender);


        //fetch data from pref
        SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        cell = sharedPreferences.getString(Constant.KEY_CELL, "");

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        //Internet connection checker
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            Toasty.error(ProfileActivity.this, "No Internet Connection", Toasty.LENGTH_LONG).show();
        } else {
            //profile information
            if (!cell.equals("")) {
                profile_cell.setText(cell);
            }
            profileInfo();
        }

    }


    //get profile
    private void profileInfo() {

        pd = new ProgressDialog(ProfileActivity.this);
        pd.setMessage("loading...");
        pd.show();

        Call<List<UserProfile>> call = apiInterface.getProfile(cell);

        call.enqueue(new Callback<List<UserProfile>>() {
            @Override
            public void onResponse(Call<List<UserProfile>> call, Response<List<UserProfile>> response) {
                if (response.isSuccessful()) {
                    pd.dismiss();
                    UserProfile userinfo = response.body().get(0);

                    profile_name.setText(userinfo.getName());
                    profile_address.setText(userinfo.getAddress());
                    profile_Gender.setText(userinfo.getGender());

                } else {
                    pd.dismiss();
                    Toasty.error(ProfileActivity.this, "try again!", Toasty.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserProfile>> call, Throwable t) {
                pd.dismiss();
                Toasty.error(ProfileActivity.this, "error!", Toasty.LENGTH_LONG).show();
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