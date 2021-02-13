package com.android.rentacar.model;

import com.google.gson.annotations.SerializedName;

public class OrderWeddingPack {

    @SerializedName("packageName")
    private String packageName;

    @SerializedName("packagePrice")
    private String packagePrice;

    @SerializedName("address")
    private String address;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("time")
    private String time;

    @SerializedName("date")
    private String date;

    @SerializedName("details")
    private String details;

    @SerializedName("pack_status")
    private String pack_status;

    @SerializedName("value")
    private String value;

    public String getPackageName() {
        return packageName;
    }

    public String getPackagePrice() {
        return packagePrice;
    }

    public String getAddress() {
        return address;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getDetails() {
        return details;
    }

    public String getPack_status() {
        return pack_status;
    }

    public String getValue() {
        return value;
    }
}
