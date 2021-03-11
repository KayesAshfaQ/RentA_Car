package com.android.rentacar.model;

import com.google.gson.annotations.SerializedName;

public class RentUserCar {


    private String model;


    private String brand;


    private String car_number;


    private String seat_no;


    private String fare;


    private String driver_name;


    private String mobile_no;


    private String status;


    @SerializedName("value")
    private String value;


//-----------------------------getter methods----------------------------------------

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public String getCar_number() {
        return car_number;
    }

    public String getSeat_no() {
        return seat_no;
    }

    public String getFare() {
        return fare;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public String getStatus() {
        return status;
    }

    public String getValue() {
        return value;
    }
}
