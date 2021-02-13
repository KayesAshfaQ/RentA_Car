package com.android.rentacar;

public class Constant {

    public static final String BASE_URL = "http://finalproject.gq/rent_a_car/android/";
    public static final String IMAGE_URL = "http://finalproject.gq/rent_a_car/admin/image/";
    public static final String CAR_IMAGE_URL = "http://finalproject.gq/rent_a_car/android/image/";
    public static final String WEDDING_PACK_IMAGE_URL = "http://finalproject.gq/rent_a_car/android/wedding_pack_img/";
    public static final String BLOG_IMAGE_URL = "http://finalproject.gq/rent_a_car/android/blog_image/";
    public static final String KEY_NAME="name";
    public static final String KEY_PASSWORD="password";
    public static final String KEY_CELL="cell";
    public static final String KEY_ACCOUNT="account";
    public static final String KEY_ADDRESS="address";
    public static final String KEY_GENDER="gender";
    public static final String KEY_TOKEN="token";
    public static final String KEY_LATITUDE="latitude";
    public static final String KEY_LONGITUDE="longitude";
    public static final String KEY_PRICE="price";
    public static final String KEY_IMAGE="image";
    public static final String KEY_AMOUNT="amount";


    //share preference
    //We will use this to store the user cell number into shared preference
    public static final String SHARED_PREF_NAME = "com.android.rentacar"; //pcakage name+ id

    //This would be used to store the cell of current logged in user
    public static final String CELL_SHARED_PREF = "cell";
    public static final String TOKEN_SHARED_PREF = "token";
    //This would be used to store the cell of current logged in user
    public static final String LATITUDE_SHARED_PREF = "latitude";

    //This would be used to store the cell of current logged in user
    public static final String LONGITUDE_SHARED_PREF = "longitude";
    //json array name.We will received data in this array
    public static final String JSON_ARRAY = "result";
}
