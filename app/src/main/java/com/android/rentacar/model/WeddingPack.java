package com.android.rentacar.model;

import com.google.gson.annotations.SerializedName;

public class WeddingPack {

    @SerializedName("id")
    private Integer id;

    @SerializedName("pack_name")
    private String pack_name;

    @SerializedName("price")
    private String price;

    @SerializedName("type")
    private String type;

    @SerializedName("image")
    private String image;

    @SerializedName("details")
    private String details;

    public Integer getId() {
        return id;
    }

    public String getPack_name() {
        return pack_name;
    }

    public String getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public String getImage() {
        return image;
    }

    public String getDetails() {
        return details;
    }
}
