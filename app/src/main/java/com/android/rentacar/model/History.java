package com.android.rentacar.model;

import com.google.gson.annotations.SerializedName;

public class History {

    @SerializedName("itemType")
    private String itemType;

    @SerializedName("itemName")
    private String itemName;

    @SerializedName("price")
    private String price;

    @SerializedName("time")
    private String time;

    @SerializedName("date")
    private String date;

    @SerializedName("status")
    private String status;

    public String getItemType() {
        return itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public String getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
