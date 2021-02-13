package com.android.rentacar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.rentacar.Constant;
import com.android.rentacar.R;
import com.android.rentacar.model.CarBook;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context context;
    private List<CarBook> carBookList;


    public HistoryAdapter(Context context, List<CarBook> carBookList) {
        this.context = context;
        this.carBookList = carBookList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        CarBook history = carBookList.get(position);

        holder.item_name.setText("Car Name: " + history.getCar_name());
        holder.item_price.setText("Total Fare: " + history.getFare());
        holder.time.setText("Time: " + history.getTrip_time());
        holder.date.setText("Date: " + history.getTrip_date());

        int stat = Integer.parseInt(history.getTrip_status());
        if (stat == 1) {
            holder.status.setText("Request: accepted");
            holder.status.setTextColor(android.R.color.holo_green_light);
        } else if (stat == 0) {
            holder.status.setText("Request: pendding");
            holder.status.setTextColor(android.R.color.holo_orange_dark);
        } else if (stat == 2) {
            holder.status.setText("Request: completed");
            holder.status.setTextColor(android.R.color.holo_green_dark);
        } else if (stat == 3) {
            holder.status.setText("Request: cancled");
            holder.status.setTextColor(android.R.color.holo_red_dark);
        }

        int typ = Integer.parseInt(history.getTrip_type());
        switch (typ) {
            case 0:
                holder.type.setText("Booking Type: Hour Basis");
                break;
            case 1:
                holder.type.setText("Booking Type: All day");
                break;
            case 2:
                holder.type.setText("Booking Type: Other");
                break;
        }

    }

    @Override
    public int getItemCount() {
        return carBookList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView type, item_name, item_price, date, time, status;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            type = itemView.findViewById(R.id.txt_type);
            item_name = itemView.findViewById(R.id.item_name);
            item_price = itemView.findViewById(R.id.item_price);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            status = itemView.findViewById(R.id.status);

        }
    }

}
