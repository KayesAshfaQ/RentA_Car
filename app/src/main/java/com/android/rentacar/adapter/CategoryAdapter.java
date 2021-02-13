package com.android.rentacar.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.rentacar.Constant;
import com.android.rentacar.shop.ProductActivity;
import com.android.rentacar.R;
import com.android.rentacar.model.Category;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>{
    private List<Category> categories;
    Context context;
    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        String uri = Constant.IMAGE_URL+categories.get(position).getImage();
        Glide.with(holder.itemView.getContext()).load(uri).into(holder.imageView);
        holder.name.setText(categories.get(position).getName());
    }

    @Override
    public int getItemCount() { return categories.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView name;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.category_iv);
            name = itemView.findViewById(R.id.category_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(context, ProductActivity.class);
            i.putExtra("id", categories.get(getAdapterPosition()).getId());
            context.startActivity(i);
        }
    }
}
