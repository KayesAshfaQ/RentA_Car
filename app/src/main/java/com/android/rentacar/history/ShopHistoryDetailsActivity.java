package com.android.rentacar.history;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.rentacar.ConnectionDetector;
import com.android.rentacar.Constant;
import com.android.rentacar.R;
import com.android.rentacar.adapter.ShopHistoryDetailsAdapter;
import com.android.rentacar.model.ShopOrder;
import com.android.rentacar.remote.ApiClient;
import com.android.rentacar.remote.ApiInterface;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopHistoryDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView orderId, totalPrice, address, name, mobile, time, status, payMethod;
    private ShopHistoryDetailsAdapter adapter;
    private List<ShopOrder> orders;
    private ApiInterface apiInterface;
    private ProgressDialog loading;
    private String mOrderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_history_details);

        orderId = findViewById(R.id.orderId);
        totalPrice = findViewById(R.id.totalPrice);
        address = findViewById(R.id.address);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        time = findViewById(R.id.time);
        status = findViewById(R.id.status);
        payMethod = findViewById(R.id.paymet_method);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recent Histories");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        recyclerView = findViewById(R.id.recyclerView);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        //Fetching data
        /*SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String phone_number = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");*/
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            mOrderID = bundle.getString("OID");
            String mTotalPrice = bundle.getString("TOTAL_PRICE");
            String mDeliveryAddress = bundle.getString("DELIVER_ADDRESS");
            String mReceiverName = bundle.getString("RECEIVER_NAME");
            String mReceiverMobile = bundle.getString("RECEIVER_MOBILE_NO");
            orderId.setText("OrderID: " + mOrderID);
            totalPrice.setText("Total Price: " + mTotalPrice);
            address.setText("Deliver Address: " + mDeliveryAddress);
            name.setText("Receiver name: " + mReceiverName);
            mobile.setText("Receiver Mobile: " + mReceiverMobile);
        }


        //Internet connection checker
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            Toasty.error(ShopHistoryDetailsActivity.this, "No Internet Connection", Toasty.LENGTH_LONG).show();
        } else {
            getHistoryData(mOrderID);
            LinearLayoutManager manager = new LinearLayoutManager(ShopHistoryDetailsActivity.this);
            recyclerView.setLayoutManager(manager);

        }

    }

    private void setMoreDetails() {
        if (orders != null){
            ShopOrder order = orders.get(0);
            time.setText("Order Time: "+order.getOrder_time());
            status.setText("Order Status: "+order.getOrder_status());
            payMethod.setText("Payment type: "+order.getPayment_type());
        }
    }

    private void getHistoryData(String orderID) {

        loading = new ProgressDialog(this);
        loading.setMessage("loading...");
        loading.show();

        Call<List<ShopOrder>> call = apiInterface.getOrderHistory(orderID);
        call.enqueue(new Callback<List<ShopOrder>>() {
            @Override
            public void onResponse(Call<List<ShopOrder>> call, Response<List<ShopOrder>> response) {
                if (response.isSuccessful()) {
                    loading.dismiss();
                    orders = response.body();
                    setMoreDetails();
                    adapter = new ShopHistoryDetailsAdapter(ShopHistoryDetailsActivity.this, orders);
                    recyclerView.setAdapter(adapter);
                    //adapter.notifyDataSetChanged();
                } else {
                    loading.dismiss();
                    Toasty.error(ShopHistoryDetailsActivity.this, "response is unsuccessful", Toasty.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ShopOrder>> call, Throwable t) {

                loading.dismiss();
                Toasty.error(ShopHistoryDetailsActivity.this, "error", Toasty.LENGTH_LONG).show();

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