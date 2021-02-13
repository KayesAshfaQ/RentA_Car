package com.android.rentacar.model;

import com.google.gson.annotations.SerializedName;

public class ShopOrder {

    @SerializedName("orderID")
    private String orderID;

    @SerializedName("price")
    private String price;

    @SerializedName("itemName")
    private String itemName;

    @SerializedName("itemAmount")
    private String itemAmount;

    @SerializedName("image")
    private String image;

    @SerializedName("order_status")
    private String order_status;

    @SerializedName("payment_type")
    private String payment_type;

    @SerializedName("order_time")
    private String order_time;

    @SerializedName("value")
    private String value;


    public String getOrderID() {
        return orderID;
    }

    public String getPrice() {
        return price;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemAmount() {
        return itemAmount;
    }

    public String getImage() {
        return image;
    }

    public String getOrder_status() {
        return order_status;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public String getOrder_time() {
        return order_time;
    }

    public String getValue() {
        return value;
    }

}
