package com.android.rentacar.remote;

import com.android.rentacar.model.RentUserCar;
import com.android.rentacar.utils.Constant;
import com.android.rentacar.model.CarBook;
import com.android.rentacar.model.Cart;
import com.android.rentacar.model.Category;
import com.android.rentacar.model.Information;
import com.android.rentacar.model.OrderWeddingPack;
import com.android.rentacar.model.Product;
import com.android.rentacar.model.ShopOrder;
import com.android.rentacar.model.ShopOrderUser;
import com.android.rentacar.model.User;
import com.android.rentacar.model.Car;
import com.android.rentacar.model.UserProfile;
import com.android.rentacar.model.WeddingPack;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    //for login
    @FormUrlEncoded
    @POST("login.php")
    Call<User> login(
            @Field(Constant.KEY_CELL) String cell,
            @Field(Constant.KEY_PASSWORD) String password,
            @Field(Constant.KEY_ACCOUNT) String account);

    //for signup
    @FormUrlEncoded
    @POST("signup.php")
    Call<User> signUp(
            @Field(Constant.KEY_NAME) String name,
            @Field(Constant.KEY_CELL) String cell,
            @Field(Constant.KEY_ACCOUNT) String account,
            @Field(Constant.KEY_PASSWORD) String password,
            @Field(Constant.KEY_ADDRESS) String address,
            @Field(Constant.KEY_GENDER) String gender,
            @Field(Constant.KEY_LATITUDE) String latitude,
            @Field(Constant.KEY_LONGITUDE) String longitude,
            @Field(Constant.KEY_TOKEN) String token);

    @FormUrlEncoded
    @POST("get_profile.php")
    Call<List<UserProfile>> getProfile(
            @Field("cell") String cell
    );

    //for book car
    @FormUrlEncoded
    @POST("car_book.php")
    Call<CarBook> bookCar(
            @Field("car_name") String car_name,
            @Field("fare") String fare,
            @Field("car_no") String car_no,
            @Field("pick_address") String pick_address,
            @Field("pick_destination") String pick_destination,
            @Field("mobile_no") String mobile_no,
            @Field("passenger_no") String passenger_no,
            @Field("trip_type") String trip_type,
            @Field("trip_time") String trip_time,
            @Field("trip_date") String trip_date,
            @Field("trip_status") String trip_status
    );


    //for getting history
    @GET("get_information_blog.php")
    Call<List<Information>> getInformation();


    //for getting Information
    @GET("get_car_book.php")
    Call<List<CarBook>> getCarBook();

    /*//for set history
    @FormUrlEncoded
    @POST("history.php")
    Call<History> setHistory(
            @Field("itemType") String itemType,
            @Field("itemName") String itemName,
            @Field("price") String price,
            @Field("time") String time,
            @Field("date") String date
    );

    //for getting history
    @GET("get_history.php")
    Call<List<History>> getHistory();*/

    //for order wedding pack
    @FormUrlEncoded
    @POST("set_wedding_pack_order.php")
    Call<OrderWeddingPack> orderPack(
            @Field("packageName") String packageName,
            @Field("packagePrice") String packagePrice,
            @Field("address") String address,
            @Field("mobile") String mobile,
            @Field("time") String time,
            @Field("date") String date,
            @Field("details") String details,
            @Field("pack_status") String pack_status
    );

    //for getting history of ordered wedding package
    @FormUrlEncoded
    @POST("get_wedding_pack_order.php")
    Call<List<OrderWeddingPack>> getorderPack(@Field("mobile") String mobile);

    //for getting history of ordered wedding package
    @GET("get_wedding_pack_order.php")
    Call<List<OrderWeddingPack>> getOrderedWeddingPack();

    //for getting cars data
    @GET("get_car.php")
    Call<List<Car>> getCar();

    //for getting wedding_pack data
    @GET("wedding_pack.php")
    Call<List<WeddingPack>> getWeddingPack();

    @GET("get_category.php")
    Call<List<Category>> getCategory(
            @Query("name") String name,
            @Query("image") String image
    );

    @GET("get_product.php")
    Call<List<Product>> getProduct(
            @Query("id") String id,
            @Query("name") String name,
            @Query("image") String image
    );

    @GET("get_item.php")
    Call<List<Cart>> getCartItem(
            @Query("cell") String cell
    );

    @FormUrlEncoded
    @POST("cart.php")
    Call<Cart> addToCart(
            @Field(Constant.KEY_NAME) String name,
            @Field(Constant.KEY_PRICE) String price,
            @Field(Constant.KEY_IMAGE) String image,
            @Field(Constant.KEY_AMOUNT) String amount,
            @Field(Constant.KEY_CELL) String cell);

    @FormUrlEncoded
    @POST("cart_item_amount_update.php")
    Call<Cart> updateCartAmount(
            @Field(Constant.KEY_AMOUNT) String amount,
            @Field(Constant.KEY_CELL) String cell
    );

    @FormUrlEncoded
    @POST("cart_remove_item.php")
    Call<Cart> removeCartItem(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("cart_remove_all_item.php")
    Call<Cart> removeAllCartItem(
            @Field("cell") String cell
    );

    //shop order table
    @FormUrlEncoded
    @POST("set_shop_order.php")
    Call<ShopOrder> addToOrder(
            @Field("orderID") String orderID,
            @Field("price") String price,
            @Field("itemName") String itemName,
            @Field("itemAmount") String itemAmount,
            @Field("image") String image,
            @Field("payment_type") String payment_type,
            @Field("order_status") String order_status,
            @Field("order_time") String order_time
    );

    @GET("get_shop_order.php")
    Call<List<ShopOrder>> getOrderHistory(@Query("orderID") String orderID);

    @FormUrlEncoded
    @POST("set_shop_order_userinfo.php")
    Call<ShopOrderUser> addUserToOrder(
            @Field("orderID") String orderID,
            @Field("username") String username,
            @Field("address") String address,
            @Field("mobile") String mobile,
            @Field("order_time") String order_time,
            @Field("total_price") String total_price,
            @Field("order_status") String order_status);

    @GET("get_shop_order_userinfo.php")
    Call<List<ShopOrderUser>> getOrderId(
            @Query("mobile") String mobile);


    //add user rented car
    @FormUrlEncoded
    @POST("set_user_car_rent.php")
    Call<RentUserCar> addUserCar(
            @Field("model") String model,
            @Field("brand") String brand,
            @Field("car_number") String car_number,
            @Field("seat_no") String seat_no,
            @Field("fare") String fare,
            @Field("driver_name") String driver_name,
            @Field("mobile_no") String mobile_no,
            @Field("status") String status
    );

    //get rented car for user
    @GET("get_user_car_rent.php")
    Call<List<RentUserCar>> getUserCar(
            @Query("mobile_no") String mobile_no
    );

}