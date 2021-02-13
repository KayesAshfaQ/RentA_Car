package com.android.rentacar.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.rentacar.R;
import com.android.rentacar.history.ShopHistoryDetailsActivity;
import com.android.rentacar.model.ShopOrder;
import com.android.rentacar.model.ShopOrderUser;

import java.util.List;

public class ShopHistoryMainAdapter extends RecyclerView.Adapter<ShopHistoryMainAdapter.ShopHistoryMainViewHolder> {

    Context context;
    List<ShopOrderUser> orderUserList;

    public ShopHistoryMainAdapter(Context context, List<ShopOrderUser> orderUserList) {
        this.context = context;
        this.orderUserList = orderUserList;
    }

    @NonNull
    @Override
    public ShopHistoryMainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_item_shop_main, parent, false);
        return new ShopHistoryMainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopHistoryMainViewHolder holder, int position) {

        final ShopOrderUser shopOrderUser = orderUserList.get(position);

        holder.orderId.setText("OrderID: " + shopOrderUser.getOrderID());
        holder.date.setText("Order Time: " + shopOrderUser.getOrder_time());
        holder.item_price.setText("Total Price: " + shopOrderUser.getTotal_price()+" TK");
        holder.order_status.setText("Order Status: \"" + shopOrderUser.getOrder_status()+"\"");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShopHistoryDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("OID", shopOrderUser.getOrderID());
                bundle.putString("TOTAL_PRICE", shopOrderUser.getTotal_price());
                bundle.putString("DELIVER_ADDRESS", shopOrderUser.getAddress());
                bundle.putString("RECEIVER_NAME", shopOrderUser.getUsername());
                bundle.putString("RECEIVER_MOBILE_NO", shopOrderUser.getMobile());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderUserList.size();
    }

    class ShopHistoryMainViewHolder extends RecyclerView.ViewHolder {

        TextView orderId, date, item_price, order_status;

        public ShopHistoryMainViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            date = itemView.findViewById(R.id.date);
            item_price = itemView.findViewById(R.id.item_price);
            order_status = itemView.findViewById(R.id.order_status);
        }
    }
}
