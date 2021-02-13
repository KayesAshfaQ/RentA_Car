package com.android.rentacar.model;

import com.google.gson.annotations.SerializedName;

public class ShopOrderUser {

    @SerializedName("orderID")
    private String orderID;

    @SerializedName("username")
    private String username;

    @SerializedName("address")
    private String address;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("order_time")
    private String order_time;

    @SerializedName("total_price")
    private String total_price;

    @SerializedName("order_status")
    private String order_status;

    @SerializedName("value")
    private String value;

    @SerializedName("message")
    private String massage;

    public String getOrderID() {
        return orderID;
    }

    public String getUsername() {
        return username;
    }

    public String getAddress() {
        return address;
    }

    public String getMobile() {
        return mobile;
    }

    public String getOrder_time() {
        return order_time;
    }

    public String getTotal_price() {
        return total_price;
    }

    public String getOrder_status() {
        return order_status;
    }

    public String getValue() {
        return value;
    }

    public String getMassage() {
        return massage;
    }
}
