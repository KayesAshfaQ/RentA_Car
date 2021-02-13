package com.android.rentacar.model;

import com.google.gson.annotations.SerializedName;

public class CarBook {

    @SerializedName("car_name")
    private String car_name;
    @SerializedName("fare")
    private String fare;
    @SerializedName("car_no")
    private String car_no;
    @SerializedName("pick_address")
    private String pick_address;
    @SerializedName("pick_destination")
    private String pick_destination;
    @SerializedName("mobile_no")
    private String mobile_no;
    @SerializedName("passenger_no")
    private String passenger_no;
    @SerializedName("trip_type")
    private String trip_type;
    @SerializedName("trip_date")
    private String trip_date;
    @SerializedName("trip_time")
    private String trip_time;

    @SerializedName("trip_status")
    private String trip_status;

    @SerializedName("value")
    private String value;


    public String getCar_name() {
        return car_name;
    }

    public String getFare() {
        return fare;
    }

    public String getCar_no() {
        return car_no;
    }

    public String getPick_address() {
        return pick_address;
    }

    public String getPick_destination() {
        return pick_destination;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public String getPassenger_no() {
        return passenger_no;
    }

    public String getTrip_type() {
        return trip_type;
    }

    public String getTrip_date() {
        return trip_date;
    }

    public String getTrip_time() {
        return trip_time;
    }

    public String getTrip_status() {
        return trip_status;
    }

    public String getValue() {
        return value;
    }
}
