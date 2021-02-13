package com.android.rentacar.model;

import com.google.gson.annotations.SerializedName;

public class Cart {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private String image;

    @SerializedName("price")
    private String price;

    @SerializedName("amount")
    private String amount;

    @SerializedName("cell")
    private String cell;

    @SerializedName("value")
    private String value;

    @SerializedName("message")
    private String massage;

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }

    public String getAmount() {
        return amount;
    }

    public String getValue() {
        return value;
    }

    public String getMassage() {
        return massage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCell() {
        return cell;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }
}
