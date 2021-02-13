package com.android.rentacar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.rentacar.Constant;
import com.android.rentacar.R;
import com.android.rentacar.model.ShopOrder;
import com.bumptech.glide.Glide;

import java.util.List;

public class ShopHistoryDetailsAdapter extends RecyclerView.Adapter<ShopHistoryDetailsAdapter.ShopHistoryViewHolder> {

    Context context;
    List<ShopOrder> orderList;

    public ShopHistoryDetailsAdapter(Context context, List<ShopOrder> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ShopHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_item_shop, parent, false);
        return new ShopHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopHistoryViewHolder holder, int position) {

        ShopOrder order = orderList.get(position);

        String uri = Constant.IMAGE_URL + order.getImage();
        Glide.with(context).load(uri).into(holder.item_image);

        holder.item_no.setText("Item No. : " + (position+1));
        holder.item_name.setText("Product name: " + order.getItemName());
        holder.item_amount.setText("amount: " + order.getItemAmount() + " pc.");
        holder.item_price.setText("price: " + order.getPrice() + "tk/pc");

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class ShopHistoryViewHolder extends RecyclerView.ViewHolder {

        ImageView item_image;
        TextView item_name, item_amount, item_price, item_no;

        public ShopHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.item_image);
            item_name = itemView.findViewById(R.id.item_name);
            item_amount = itemView.findViewById(R.id.item_amount);
            item_price = itemView.findViewById(R.id.item_price);
            item_no = itemView.findViewById(R.id.item_no);
        }
    }

}
