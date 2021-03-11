package com.android.rentacar.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.rentacar.utils.Constant;
import com.android.rentacar.R;
import com.android.rentacar.wedding_package.WeddingPackDetailsActivity;
import com.android.rentacar.wedding_package.WeddingPackOrder;
import com.android.rentacar.model.WeddingPack;
import com.bumptech.glide.Glide;

import java.util.List;

public class WeddingPackAdapter extends RecyclerView.Adapter<WeddingPackAdapter.WeddingPackViewHolder> {

    private Context context;
    private List<WeddingPack> weddingPackList;

    public WeddingPackAdapter(Context context, List<WeddingPack> weddingPackList) {
        this.context = context;
        this.weddingPackList = weddingPackList;
    }

    @NonNull
    @Override
    public WeddingPackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wedding_package_item, parent, false);
        return new WeddingPackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeddingPackViewHolder holder, int position) {

        final WeddingPack weddingPack = weddingPackList.get(position);

        String uri = Constant.WEDDING_PACK_IMAGE_URL + weddingPack.getImage();
        Glide.with(context).load(uri).into(holder.pack_image);

        holder.pack_number.setText("Package No. "+weddingPack.getId());
        holder.pack_name.setText(weddingPack.getPack_name());
        holder.type.setText(weddingPack.getType());
        holder.price.setText("Price: " + weddingPack.getPrice() + "TK");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, WeddingPackDetailsActivity.class);
                intent.putExtra("package_id", weddingPack.getId());
                intent.putExtra("package_name", weddingPack.getPack_name());
                intent.putExtra("package_price", weddingPack.getPrice());
                intent.putExtra("details", weddingPack.getDetails());
                intent.putExtra("image", weddingPack.getImage());
                context.startActivity(intent);

            }
        });

        holder.book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, WeddingPackOrder.class);
                intent.putExtra("package_id", weddingPack.getId());
                intent.putExtra("package_name", weddingPack.getPack_name());
                intent.putExtra("package_price", weddingPack.getPrice());
                intent.putExtra("image", weddingPack.getImage());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return weddingPackList.size();
    }

    public class WeddingPackViewHolder extends RecyclerView.ViewHolder {

        public ImageView pack_image;
        public TextView pack_name, type, price, pack_number;
        public Button book_now;

        public WeddingPackViewHolder(@NonNull View itemView) {
            super(itemView);

            pack_image = itemView.findViewById(R.id.pack_image);
            pack_number = itemView.findViewById(R.id.pack_number);
            pack_name = itemView.findViewById(R.id.pack_name);
            type = itemView.findViewById(R.id.type);
            price = itemView.findViewById(R.id.price);
            book_now = itemView.findViewById(R.id.book_now);

        }
    }

}
