package com.example.ass2_i200802_i200707;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>
{
    private Context context;
    private List<ItemModel> ls;

    public ItemAdapter(Context context, List<ItemModel> ls) {
        this.context = context;
        this.ls = ls;
    }

    @NonNull
    @Override
    public ItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(context)
                .inflate(R.layout.item_view,parent,false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ItemModel itemList = ls.get(position);
        String imgUrl = itemList.getImgUrl();
        Picasso.get().load(imgUrl).into(holder.itemImg);

        holder.itemName.setText(ls.get(position).getItemName());
        String price = "$" + ls.get(position).getItemPrice() + "/hr";
        holder.itemPrice.setText(price);
        holder.itemDate.setText(ls.get(position).getItemDate());

        holder.itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Item.class);
                intent.putExtra("itemName",ls.get(position).getItemName());
                intent.putExtra("itemPrice",ls.get(position).getItemPrice());
                intent.putExtra("itemDate",ls.get(position).getItemDate());
                intent.putExtra("itemImg",ls.get(position).getImgUrl());
                intent.putExtra("userId",ls.get(position).getUserId());
                intent.putExtra("itemId", ls.get(position).getItemId());
                intent.putExtra("itemDescription", ls.get(position).getItemDescription());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice, itemDate;
        ImageView itemImg;
        CardView itemCard;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemDate = itemView.findViewById(R.id.itemDate);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemImg = itemView.findViewById(R.id.itemImg);
            itemCard = itemView.findViewById(R.id.itemView);
        }
    }
}

