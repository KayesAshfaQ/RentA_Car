package com.android.rentacar.model;

import com.google.gson.annotations.SerializedName;

public class UserProfile {

    @SerializedName("name")
    private String name;

    @SerializedName("address")
    private String address;

    @SerializedName("gender")
    private String gender;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }
}
