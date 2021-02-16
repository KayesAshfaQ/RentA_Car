package com.android.rentacar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.rentacar.model.User;
import com.android.rentacar.remote.ApiClient;
import com.android.rentacar.remote.ApiInterface;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    Button registerBtn, loginBtn;
    TextView GotoLoginTv;
    private List<User> areaList;
    public Calendar myCalendar = Calendar.getInstance();
    public DatePickerDialog.OnDateSetListener appointment_date;
    private int mYear, mMonth, mDay;
    ArrayList<String> areaNames = new ArrayList<String>();
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    EditText etxtName, etxtCell, etxtPassword, etxtArea, etxtGender, etxtAccount;
    private ApiInterface apiInterface;
    String text = "", user_name, user_cell, user_password, user_area, user_gender, lat, lng, user_token = "", user_account;
    private ProgressDialog loading;
    private static final String CHANNEL_ID = "rent_a_car";
    private static final String CHANNEL_NAME = "Rent a Car";
    private static final String CHANNEL_DESC = "Android Push Notification Tutorial";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rent a Car");


        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        registerBtn = findViewById(R.id.signup_button);
        loginBtn = findViewById(R.id.login_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        //create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        //Runtime permissions
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            getLocation();
        }


        etxtName = findViewById(R.id.name_edittext);
        etxtName.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if (cs.equals("")) { // for backspace
                            return cs;
                        }
                        if (cs.toString().matches("[a-zA-Z ]+")) {
                            return cs;
                        }
                        return "";
                    }
                }
        });
        etxtCell = findViewById(R.id.phone_edittext);
        etxtPassword = findViewById(R.id.password_edittext);
        etxtArea = findViewById(R.id.address_edittext);
        etxtGender = findViewById(R.id.gender_edittext);
        //etxtAccount = findViewById(R.id.account_edittext);

        //gender selection dialog
        etxtGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] genderList = {"Male", "Female"};

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Choose Gender");
                builder.setCancelable(false);
                builder.setItems(genderList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0:
                                etxtGender.setText(genderList[position]);
                                text = genderList[position];
                                break;

                            case 1:
                                etxtGender.setText(genderList[position]);
                                text = genderList[position];
                                break;

                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        dialog.dismiss();
                    }
                });


                AlertDialog accountTypeDialog = builder.create();

                accountTypeDialog.show();
            }

        });

        //account type selection dialog
        /*etxtAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] accountList = {"User", "Driver"};

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Choose Account");
                builder.setCancelable(false);
                builder.setItems(accountList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0:
                                etxtAccount.setText(accountList[position]);
                                text = accountList[position];
                                break;

                            case 1:
                                etxtAccount.setText(accountList[position]);
                                text = accountList[position];
                                break;

                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        dialog.dismiss();
                    }
                });

                AlertDialog accountTypeDialog = builder.create();
                accountTypeDialog.show();
            }

        });*/


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Internet connection checker
                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                // Check if Internet present
                if (!cd.isConnectingToInternet()) {
                    // Internet Connection is not present
                    Toasty.error(RegisterActivity.this, "No Internet Connection", Toasty.LENGTH_LONG).show();
                } else {
                    //taking values
                    user_name = etxtName.getText().toString();
                    user_cell = etxtCell.getText().toString();
                    user_password = etxtPassword.getText().toString();
                    user_area = etxtArea.getText().toString();
                    //user_account = etxtAccount.getText().toString();
                    user_gender = etxtGender.getText().toString();

                    //validation
                    if (user_name.isEmpty()) {
                        etxtName.setError("Name can not be empty! ");
                        etxtName.requestFocus();

                    } else if (user_cell.length() != 11 || !user_cell.startsWith("01")) {
                        etxtCell.setError("Invalid cell!");
                        etxtCell.requestFocus();

                    } else if (user_password.isEmpty()) {
                        etxtPassword.setError("Password can not be empty! ");
                        etxtPassword.requestFocus();

                    } else if ((user_password.length() < 4)) {
                        etxtPassword.setError("Password should be more than 3 characters!");
                        etxtPassword.requestFocus();

                    } else if (user_area.isEmpty()) {
                        etxtArea.setError("Address can not be empty! ");
                        etxtArea.requestFocus();
                    } else if (user_gender.isEmpty()) {
                        etxtGender.setError("Gender can't be empty! ");
                        etxtGender.requestFocus();
                        Toasty.error(RegisterActivity.this, "Gender can't be empty! ", Toast.LENGTH_SHORT).show();
                    }
                    //Runtime permissions
                    else {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION
                            }, REQUEST_CODE_LOCATION_PERMISSION);
                        } else {
                            getLocation();
                        }

                        //call signup method
                        sign_up(user_name, user_cell, "User", user_password, user_area, user_gender, lat, lng, user_token);
                    }
                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toasty.error(this, "Permission Denied!", Toasty.LENGTH_SHORT).show();
            }
        }
    }

    private void getLocation() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(RegisterActivity.this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(RegisterActivity.this).removeLocationUpdates(this);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int latestLocationIndex = locationResult.getLocations().size() - 1;
                    double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                    double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                    lat = String.valueOf(latitude);
                    lng = String.valueOf(longitude);
                }
            }
        }, Looper.getMainLooper());
    }

    //signup method
    private void sign_up(String name, String cell, String account, String password, String address, String gender,
                         String latitude, String longitude, String token) {

        loading = new ProgressDialog(this);
        loading.setMessage("Please wait....");
        loading.show();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<User> call = apiInterface.signUp(name, cell, account, password, address, gender, latitude, longitude, token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("success")) {
                    loading.dismiss();
                    Toasty.success(RegisterActivity.this, message, Toasty.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);

                } else if (value.equals("exists")) {
                    loading.dismiss();
                    Toasty.error(RegisterActivity.this, message, Toasty.LENGTH_SHORT).show();


                } else {
                    loading.dismiss();
                    Toasty.error(RegisterActivity.this, message, Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                loading.dismiss();
                Toasty.error(RegisterActivity.this, "Error! " + t.toString(), Toasty.LENGTH_SHORT).show();
            }
        });
    }

}