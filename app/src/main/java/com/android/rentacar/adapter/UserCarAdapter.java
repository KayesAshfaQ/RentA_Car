package com.android.rentacar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.rentacar.R;
import com.android.rentacar.model.RentUserCar;

import java.util.List;

public class UserCarAdapter extends RecyclerView.Adapter<UserCarAdapter.ViewHolder> {

    private Context context;
    private List<RentUserCar> carList;

    public UserCarAdapter(Context context, List<RentUserCar> carList) {
        this.context = context;
        this.carList = carList;
    }

    public UserCarAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_car_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.item_brand.setText(carList.get(position).getBrand());
        holder.item_model.setText(carList.get(position).getModel());
        holder.driver_name.setText(carList.get(position).getDriver_name());
        holder.car_number.setText(carList.get(position).getCar_number());


        String s = carList.get(position).getStatus();
        if (!s.isEmpty()) {
            int status = Integer.parseInt(s);
            if (status == 0) {
                holder.status.setText("Status: cancel");
            } else if (status == 1) {
                holder.status.setText("Status: accepted");
            } else if (status == 2) {
                holder.status.setText("Status: pending");
            } else {
                holder.status.setText("Status: error!");
            }
        }


    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView item_brand, item_model, driver_name, car_number, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_brand = itemView.findViewById(R.id.item_brand);
            item_model = itemView.findViewById(R.id.item_model);
            driver_name = itemView.findViewById(R.id.driver_name);
            car_number = itemView.findViewById(R.id.car_number);
            status = itemView.findViewById(R.id.status);
        }
    }

}
