package com.android.rentacar.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.rentacar.Constant;
import com.android.rentacar.R;
import com.android.rentacar.model.Cart;
import com.android.rentacar.remote.ApiClient;
import com.android.rentacar.remote.ApiInterface;
import com.android.rentacar.shop.CartActivity;
import com.android.rentacar.shop.ProductDetailsActivity;
import com.bumptech.glide.Glide;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    Context context;
    private List<Cart> carts;

    public CartAdapter(Context context, List<Cart> carts) {
        this.context = context;
        this.carts = carts;
    }

    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartAdapter.MyViewHolder holder, final int position) {

        holder.name.setText(carts.get(position).getName());
        holder.price.setText("Tk " + carts.get(position).getPrice());
        holder.amount.setText("Amount:" + carts.get(position).getAmount());
        String uri = Constant.IMAGE_URL + carts.get(position).getImage();
        Glide.with(holder.itemView.getContext()).load(uri).into(holder.image);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(carts.get(position).getId());
                carts.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(0, carts.size());
            }
        });



    }

    private void removeItem(String id) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        /*//fetch data from pref
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String cell = sharedPreferences.getString(Constant.KEY_CELL, "");
*/
        Call<Cart> call = apiInterface.removeCartItem(id);

        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {

                if (response.isSuccessful()) {
                    String value = response.body().getValue();
                    String message = response.body().getMassage();
                    if (!value.isEmpty() && !message.isEmpty()) {
                        if (value.equals("success")) {
                            Toasty.success(context, message, Toasty.LENGTH_SHORT).show();
                        } else {
                            Toasty.error(context, message, Toasty.LENGTH_SHORT).show();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                Toasty.error(context, "error! \n failed to remove item!", Toasty.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, price, amount;
        ImageView image, delete;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name_tv);
            price = itemView.findViewById(R.id.item_price_tv);
            image = itemView.findViewById(R.id.item_image_iv);
            delete = itemView.findViewById(R.id.delete_item_iv);
            amount = itemView.findViewById(R.id.amount);

        }


    }
}
