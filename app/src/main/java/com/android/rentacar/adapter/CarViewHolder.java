package com.android.rentacar.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.rentacar.R;

public class CarViewHolder extends RecyclerView.ViewHolder {

    public ImageView car_image;
    public TextView car_name, car_seat, car_number, driver_name, fare;
    public Button book_now;

    public CarViewHolder(@NonNull View itemView) {
        super(itemView);

        car_image = itemView.findViewById(R.id.car_image);
        car_name = itemView.findViewById(R.id.car_name);
        car_seat = itemView.findViewById(R.id.car_seat);
        car_number = itemView.findViewById(R.id.car_number);
        driver_name = itemView.findViewById(R.id.driver_name);
        fare = itemView.findViewById(R.id.fare);
        book_now = itemView.findViewById(R.id.book_now);

    }
}
