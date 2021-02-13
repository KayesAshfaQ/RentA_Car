package com.android.rentacar.model;

import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private String image;
    @SerializedName("price")
    private String price;
    @SerializedName("details")
    private String details;
    @SerializedName("category_id")
    private String category_id;
    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }

    public String getDetails() {
        return details;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getId() {
        return id;
    }
}
