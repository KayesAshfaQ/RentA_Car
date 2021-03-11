package com.android.rentacar.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.rentacar.utils.ConnectionDetector;
import com.android.rentacar.utils.Constant;
import com.android.rentacar.MainActivity;
import com.android.rentacar.R;
import com.android.rentacar.model.Cart;
import com.android.rentacar.model.ShopOrder;
import com.android.rentacar.model.ShopOrderUser;
import com.android.rentacar.remote.ApiClient;
import com.android.rentacar.remote.ApiInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    private ImageView product_image;
    private TextView total_price, total_items, deliver_time, deliver_date;
    private EditText address_edt, mobile_no, reciver_name;
    private String address, name, mobile;
    private Button confirm_order;
    private ApiInterface apiInterface;
    private List<Cart> cartList;
    private String paymentType = "";
    private ProgressDialog progressDialog;
    private int totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        product_image = findViewById(R.id.product_image);

        total_price = findViewById(R.id.total_price);
        total_items = findViewById(R.id.total_items);
        deliver_time = findViewById(R.id.deliver_time);
        deliver_date = findViewById(R.id.deliver_date);

        address_edt = findViewById(R.id.address);
        mobile_no = findViewById(R.id.mobile_no);
        reciver_name = findViewById(R.id.reciver_name);
        confirm_order = findViewById(R.id.confirm_order);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        progressDialog = new ProgressDialog(OrderActivity.this);

        //Fetching cell from shared preferences
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String getCell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");
        //Internet connection checker
        final ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(OrderActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
        } else {
            if (!getCell.isEmpty()) {
                getCartItem(getCell);
                mobile_no.setText(getCell);
            }
        }


        //paymet method
        final RadioGroup radioGroup = findViewById(R.id.paymet_method);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.cash:
                        paymentType = "cash";
                        Toasty.success(OrderActivity.this, "Cash payment selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.bikash:
                        paymentType = "bKash";
                        Toasty.success(OrderActivity.this, "Bikash payment selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nagad:
                        paymentType = "nagad";
                        Toasty.success(OrderActivity.this, "Nagad payment selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.card:
                        paymentType = "card";
                        Toasty.success(OrderActivity.this, "Card payment selected", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        //confirm button
        confirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address = address_edt.getText().toString();
                name = reciver_name.getText().toString();
                mobile = mobile_no.getText().toString();
                //validation
                if (address.isEmpty()) {
                    address_edt.setError("please enter Receiver name ");
                    address_edt.requestFocus();
                } else {
                    if (mobile_no.length() != 11) {
                        mobile_no.setError("Invalid Mobile Number!");
                        mobile_no.requestFocus();
                    } else {
                        if (name.isEmpty()) {
                            reciver_name.setError("please enter Deliver Address ");
                            reciver_name.requestFocus();
                        } else {
                            String orderId = null;
                            //Internet connection checker
                            if (!cd.isConnectingToInternet()) {
                                Toast.makeText(OrderActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                            } else {
                                progressDialog.setMessage("ordering...");
                                progressDialog.show();
                                orderId = "OID_" + System.currentTimeMillis();
                                //UUID.randomUUID().toString().replace("-", "");
                                for (int i = 0; i < cartList.size(); i++) {
                                    sendToOrderTable(
                                            orderId,
                                            cartList.get(i).getPrice(),
                                            cartList.get(i).getName(),
                                            cartList.get(i).getAmount(),
                                            cartList.get(i).getImage(),
                                            paymentType,
                                            getDateTime()
                                    );
                                }
                                sendToUserOrder(orderId, name, address, mobile, getDateTime());
                                clearUserCart(getCell);
                            }
                        }
                    }
                }
            }
        });

        //date & time
        deliver_time.append(getDateTime());
        deliver_date.append("\nYour product will be delivered with in seven days from the order-date.Thank you...");

    }

    private void clearUserCart(String cell) {

        Call<Cart> cartCall = apiInterface.removeAllCartItem(cell);

        cartCall.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (response.isSuccessful()) {
                    String value = response.body().getValue();
                    if (value != null) {
                        if (value.equals("success")) {
                            startActivity(new Intent(OrderActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                } else {
                    Toast.makeText(OrderActivity.this, "cart remove response unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                Toast.makeText(OrderActivity.this, "cart not cleaned", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getCartItem(String cell) {
        Call<List<Cart>> call = apiInterface.getCartItem(cell);
        call.enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                progressDialog.setMessage("loading...");
                progressDialog.show();
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    cartList = response.body();
                    if (cartList != null) {
                        totalPrice = 0;
                        for (int i = 0; i < cartList.size(); i++) {
                            if (Integer.parseInt(cartList.get(i).getAmount()) > 1) {
                                totalPrice = (Integer.parseInt(cartList.get(i).getPrice()) * Integer.parseInt(cartList.get(i).getAmount())) + totalPrice;
                            } else {
                                totalPrice = Integer.parseInt(cartList.get(i).getPrice()) + totalPrice;
                            }

                            total_items.append("Item " + (i + 1) + "- " + cartList.get(i).getName() + " Price: " + cartList.get(i).getPrice() + "tk\n");

                        }
                        total_price.setText(String.valueOf(totalPrice) + " BDT");
                    }
                } else {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(OrderActivity.this, "Error : " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void sendToOrderTable(final String orderId, String price, String itemName, String itemAmount,
                                  String image, String payment_type, String order_time) {

        Call<ShopOrder> call = apiInterface.addToOrder(orderId, price, itemName, itemAmount, image, payment_type, "pending", order_time);

        call.enqueue(new Callback<ShopOrder>() {
            @Override
            public void onResponse(Call<ShopOrder> call, Response<ShopOrder> response) {
                if (response.isSuccessful()) {
                    String value = response.body().getValue();
                    if (value.equals("failure")) {
                        progressDialog.dismiss();
                        Toasty.error(OrderActivity.this, "failed to order", Toasty.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ShopOrder> call, Throwable t) {
                progressDialog.dismiss();
                Toasty.error(OrderActivity.this, "Error! " + t.toString(), Toasty.LENGTH_SHORT).show();
            }
        });


    }

    private void sendToUserOrder(String orderId, String username, String address, String mobile, String time) {

        Call<ShopOrderUser> call = apiInterface.addUserToOrder(orderId, username, address, mobile, time, String.valueOf(totalPrice), "pending");
        call.enqueue(new Callback<ShopOrderUser>() {
            @Override
            public void onResponse(Call<ShopOrderUser> call, Response<ShopOrderUser> response) {
                if (response.isSuccessful()) {
                    String value = response.body().getValue();
                    if (value.equals("success")) {
                        progressDialog.dismiss();
                        Toasty.success(OrderActivity.this, "successfully ordered ", Toasty.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toasty.error(OrderActivity.this, "failure ", Toasty.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ShopOrderUser> call, Throwable t) {
                progressDialog.dismiss();
                Toasty.error(OrderActivity.this, "Error! " + t.toString(), Toasty.LENGTH_SHORT).show();
            }
        });

    }
}