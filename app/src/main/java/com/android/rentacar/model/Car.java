package com.android.rentacar.model;

import com.google.gson.annotations.SerializedName;

public class Car {

    @SerializedName("id")
    private Integer id;

    @SerializedName("vehicle_name")
    private String vehicle_name;

    @SerializedName("seat_no")
    private String seat_no;

    @SerializedName("fare")
    private String fare;

    @SerializedName("vehicle_no")
    private String vehicle_no;

    @SerializedName("driver_name")
    private String driver_name;

    @SerializedName("image")
    private String image;

    @SerializedName("details")
    private String details;

    public Integer getId() {
        return id;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public String getSeat_no() {
        return seat_no;
    }

    public String getFare() {
        return fare;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public String getImage() {
        return image;
    }

    public String getDetails() {
        return details;
    }
}
