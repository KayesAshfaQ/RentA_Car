package com.android.rentacar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.rentacar.R;
import com.android.rentacar.model.OrderWeddingPack;

import java.util.List;

public class PackageHistoryAdapter extends RecyclerView.Adapter<PackageHistoryAdapter.HistoryViewHolder> {

    private Context context;
    private List<OrderWeddingPack> packageOrderList;

    public PackageHistoryAdapter(Context context, List<OrderWeddingPack> packageOrderList) {
        this.context = context;
        this.packageOrderList = packageOrderList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);
        return new PackageHistoryAdapter.HistoryViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {

        OrderWeddingPack history = packageOrderList.get(position);

        holder.item_name.setText("Package Name: " + history.getPackageName());
        holder.item_price.setText("Total price: " + history.getPackagePrice());
        holder.time.setText("Time: " + history.getTime());
        holder.date.setText("Date: " + history.getDate());

        int stat = Integer.parseInt(history.getPack_status());
        if (stat == 1) {
            holder.status.setText("Order: accepted");
            holder.status.setTextColor(android.R.color.holo_green_light);
        } else if (stat == 0) {
            holder.status.setText("Order: pendding");
            holder.status.setTextColor(android.R.color.holo_orange_dark);
        } else if (stat == 2) {
            holder.status.setText("Order: completed");
            holder.status.setTextColor(android.R.color.holo_green_dark);
        } else if (stat == 3) {
            holder.status.setText("Order: cancled");
            holder.status.setTextColor(android.R.color.holo_red_dark);
        }


    }

    @Override
    public int getItemCount() {
        return packageOrderList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView type, item_name, item_price, date, time, status;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.txt_type);
            item_name = itemView.findViewById(R.id.item_brand);
            item_price = itemView.findViewById(R.id.item_model);
            date = itemView.findViewById(R.id.driver_name);
            time = itemView.findViewById(R.id.car_number);
            status = itemView.findViewById(R.id.status);

        }
    }

}
