package com.android.rentacar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.rentacar.car_order.CarBookActivity;
import com.android.rentacar.car_order.CarDetailsActivity;
import com.android.rentacar.Constant;
import com.android.rentacar.R;
import com.android.rentacar.model.Car;
import com.bumptech.glide.Glide;

import java.util.List;

public class WeddingCarAdapter extends RecyclerView.Adapter<CarViewHolder> {

    private Context context;
    private List<Car> carList;

    public WeddingCarAdapter(Context context, List<Car> carList) {
        this.context = context;
        this.carList = carList;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.car_item, parent, false);
        return new CarViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {

        final Car car = carList.get(position);

        String uri = Constant.CAR_IMAGE_URL+ car.getImage();
        Glide.with(context).load(uri).into(holder.car_image);

        holder.car_name.setText(car.getVehicle_name());
        holder.car_seat.setText("Seat - "+ car.getSeat_no());
        holder.car_number.setText("Car no. - "+ car.getVehicle_no());
        holder.driver_name.setText("Driver name - "+ car.getDriver_name());
        holder.fare.setText("Rate - "+ car.getFare()+"TK/h");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, CarDetailsActivity.class);
                i.putExtra("vehicle_id", car.getId());
                i.putExtra("vehicle_name", car.getVehicle_name());
                i.putExtra("details", car.getDetails());
                i.putExtra("vehicle_fare", car.getFare());
                i.putExtra("seat_no", car.getSeat_no());
                i.putExtra("vehicle_no", car.getVehicle_no());
                i.putExtra("driver_name", car.getDriver_name());
                i.putExtra("image",car.getImage());
                i.putExtra("details",car.getDetails());
                context.startActivity(i);
            }
        });

        holder.book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, CarBookActivity.class);
                i.putExtra("vehicle_no", car.getVehicle_no());
                i.putExtra("vehicle_name", car.getVehicle_name());
                i.putExtra("vehicle_fare", car.getFare());
                i.putExtra("image",car.getImage());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

}
