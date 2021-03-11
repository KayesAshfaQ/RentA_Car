package com.android.rentacar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.rentacar.blog.InformationActivity;
import com.android.rentacar.car_order.TravelActivity;
import com.android.rentacar.car_order.WeddingActivity;
import com.android.rentacar.history.CarHistoryActivity;
import com.android.rentacar.history.HistoryWeddingPackActivity;
import com.android.rentacar.history.ShopHistoryActivity;
import com.android.rentacar.profile.ProfileActivity;
import com.android.rentacar.shop.CartActivity;
import com.android.rentacar.shop.CategoryActivity;
import com.android.rentacar.user_rent.RentUserCarActivity;
import com.android.rentacar.user_rent.UserCarHistoryActivity;
import com.android.rentacar.utils.Constant;
import com.android.rentacar.utils.MyCustomPagerAdapter;
import com.android.rentacar.wedding_package.WeddingPackActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;

    ViewPager viewPager;
    MyCustomPagerAdapter myCustomPagerAdapter;
    int images[] = {R.drawable.imgone, R.drawable.imgtwo, R.drawable.imgthree};
    int currentPage = 0, NUM_PAGES = 4;
    FloatingActionButton floatingActionButton;
    final long DELAY_MS = 1000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    LinearLayout sliderDotspanel, categoryLayout;
    private int dotscount;
    Timer timer;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        categoryLayout = (LinearLayout) findViewById(R.id.category_layout);
        floatingActionButton = findViewById(R.id.fab_cart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");
        getSupportActionBar().setHomeButtonEnabled(true);


        //navigation drawer
        drawerLayout = findViewById(R.id.activity_main_dl);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this,
                drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        //cart view one items
        View wedding_button = findViewById(R.id.wedding_layout);
        wedding_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WeddingActivity.class));
            }
        });
        View travel_button = findViewById(R.id.travel_layout);
        travel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TravelActivity.class));
            }
        });
        View wedding_pack_btn = findViewById(R.id.wedding_pack_layout);
        wedding_pack_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WeddingPackActivity.class));
            }
        });
        View information = findViewById(R.id.information_layout);
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InformationActivity.class));
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        myCustomPagerAdapter = new MyCustomPagerAdapter(MainActivity.this, images);
        viewPager.setAdapter(myCustomPagerAdapter);
        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES - 1) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);


        dotscount = myCustomPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(MainActivity.this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
        categoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;

            case R.id.shop:
                startActivity(new Intent(MainActivity.this, CategoryActivity.class));
                break;

            case R.id.TravelCar:
                startActivity(new Intent(MainActivity.this, TravelActivity.class));
                break;

            case R.id.WeddingCar:
                startActivity(new Intent(MainActivity.this, WeddingActivity.class));
                break;

            case R.id.packages:
                startActivity(new Intent(MainActivity.this, WeddingPackActivity.class));
                break;

            case R.id.user_car:
                startActivity(new Intent(MainActivity.this, RentUserCarActivity.class));
                break;

            case R.id.logout: {

                AlertDialog.Builder logoutBuilder = new AlertDialog.Builder(MainActivity.this);
                logoutBuilder.setMessage("Do you want to logout?");
                logoutBuilder.setIcon(R.drawable.ic_logout_small_black);
                logoutBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toasty.error(getApplicationContext(), "logout done").show();

                        SharedPreferences preferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();

                        SharedPreferences preferences2 = getSharedPreferences("prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = preferences2.edit();
                        editor2.clear();

                        editor.commit();
                        editor2.commit();

                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                });

                logoutBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toasty.error(getApplicationContext(), "logout canceled!").show();
                    }
                });

                logoutBuilder.create().show();
                break;

            }

            case R.id.history:
                startActivity(new Intent(MainActivity.this, CarHistoryActivity.class));
                break;


            case R.id.wedding_car_history:
                startActivity(new Intent(MainActivity.this, HistoryWeddingPackActivity.class));
                break;

            case R.id.user_car_history:
                startActivity(new Intent(MainActivity.this, UserCarHistoryActivity.class));
                break;

            case R.id.shop_history:
                startActivity(new Intent(MainActivity.this, ShopHistoryActivity.class));
                break;

            default:
                return true;
        }

        return true;
    }
}