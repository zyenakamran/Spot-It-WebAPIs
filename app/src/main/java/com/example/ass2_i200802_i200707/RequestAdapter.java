package com.example.ass2_i200802_i200707;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ItemViewHolder> {
    List<RequestModel> itemList;

    Context context;

    public RequestAdapter(List<RequestModel> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RequestAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rent_items_view, parent, false);
        return new RequestAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RequestModel item = itemList.get(position);

        String imgUrl = item.getItemImg();
        Picasso.get().load(imgUrl).into(holder.itemImg);
        holder.itemName.setText(item.getItemName());
        holder.requesterId.setText(item.getRequesterId());

        holder.requestItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, Item.class);
                intent.putExtra("itemName",itemList.get(position).getItemName());
                intent.putExtra("requesterId",itemList.get(position).getRequesterId());
                intent.putExtra("userId",itemList.get(position).getUserId());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, requesterId, itemPrice;
        ImageView acceptButton, rejectButton, itemImg;
        LinearLayout requestItemView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.itemName);
            requesterId = itemView.findViewById(R.id.requestId);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
            itemImg = itemView.findViewById(R.id.itemImg);
            requestItemView = itemView.findViewById(R.id.requestItemView);

            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "Request accepted", Toast.LENGTH_SHORT).show();
                }
            });

            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "Request rejected", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
