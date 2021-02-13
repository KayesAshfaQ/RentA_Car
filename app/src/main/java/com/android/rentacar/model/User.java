package com.android.rentacar.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("name")
    private String name;
    @SerializedName("cell")
    private String cell;
    @SerializedName("password")
    private String password;
    @SerializedName("gender")
    private String gender;
    @SerializedName("address")
    private String address;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("value")
    private String value;
    @SerializedName("message")
    private String massage;
    @SerializedName("token")
    private String token;

    public String getName() {
        return name;
    }

    public String getCell() {
        return cell;
    }

    public String getPassword() {
        return password;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getValue() {
        return value;
    }

    public String getMassage() {
        return massage;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getToken() {
        return token;
    }
}