package com.android.rentacar.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.rentacar.utils.Constant;
import com.android.rentacar.blog.InformationDetailsActivity;
import com.android.rentacar.R;
import com.android.rentacar.model.Information;
import com.bumptech.glide.Glide;

import java.util.List;

public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.InformationViewHolder> {

    private Context context;
    private List<Information> informationList;

    public InformationAdapter(Context context, List<Information> informationList) {
        this.context = context;
        this.informationList = informationList;
    }

    @NonNull
    @Override
    public InformationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.blog_item, parent, false);
        return new InformationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InformationViewHolder holder, int position) {
        final Information information = informationList.get(position);

        Glide.with(holder.imageView.getContext()).load(Constant.BLOG_IMAGE_URL +information.getImage()).into(holder.imageView);
        holder.title.setText(information.getTitle());
        holder.info.setText(information.getInfo());
        holder.time.setText("Time: "+information.getTime());
        holder.date.setText("Date: "+information.getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InformationDetailsActivity.class);
                intent.putExtra("image", information.getImage());
                intent.putExtra("title", information.getTitle());
                intent.putExtra("date", information.getDate());
                intent.putExtra("time", information.getTime());
                intent.putExtra("details", information.getInfo());
                intent.putExtra("link", information.getLink());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return informationList.size();
    }

    public class InformationViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title, info, date, time;

        public InformationViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.blog_image);
            title = itemView.findViewById(R.id.title);
            info = itemView.findViewById(R.id.info);
            date = itemView.findViewById(R.id.driver_name);
            time = itemView.findViewById(R.id.car_number);

        }
    }

}
