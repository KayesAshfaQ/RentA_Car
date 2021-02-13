package com.android.rentacar.model;

import com.google.gson.annotations.SerializedName;

public class Information {

    @SerializedName("title")
    private String title;

    @SerializedName("date")
    private String date;

    @SerializedName("time")
    private String time;

    @SerializedName("image")
    private String image;

    @SerializedName("info")
    private String info;

    @SerializedName("link")
    private String link;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
