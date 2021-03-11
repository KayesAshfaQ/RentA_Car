package com.android.rentacar.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.rentacar.utils.Constant;
import com.android.rentacar.shop.ProductDetailsActivity;
import com.android.rentacar.R;
import com.android.rentacar.model.Product;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder>{
    private List<Product> products;
    Context context;
    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new ProductAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.MyViewHolder holder, int position) {
        String uri = Constant.IMAGE_URL+ products.get(position).getImage();
        Glide.with(holder.itemView.getContext()).load(uri).into(holder.imageView);
        holder.name.setText(products.get(position).getName());
    }

    @Override
    public int getItemCount() { return products.size(); }

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
            Intent i = new Intent(context, ProductDetailsActivity.class);
            i.putExtra("id", products.get(getAdapterPosition()).getId());
            i.putExtra("name", products.get(getAdapterPosition()).getName());
            i.putExtra("details", products.get(getAdapterPosition()).getDetails());
            i.putExtra("price", products.get(getAdapterPosition()).getPrice());
            i.putExtra("image", products.get(getAdapterPosition()).getImage());
            context.startActivity(i);
        }
    }
}
