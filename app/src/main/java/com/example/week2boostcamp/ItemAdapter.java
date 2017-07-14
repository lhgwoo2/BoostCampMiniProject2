package com.example.week2boostcamp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 현기 on 2017-07-14.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    ArrayList<Item> list;

    public ItemAdapter(ArrayList<Item> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Item item = list.get(position);

        holder.foodCheckView.setChecked(item.checked);
        holder.foodImageView.setImageResource(item.imgId);
        holder.foodContentsView.setText(item.contents);
        holder.foodTitleView.setText(item.title);

        //체크박스 변경된 데이터 저장
        holder.foodCheckView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = holder.getAdapterPosition();
                list.get(position).checked = !list.get(position).checked;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView foodTitleView;
        ImageView foodImageView;
        TextView foodContentsView;
        CheckBox foodCheckView;

        public ViewHolder(View itemView) {
            super(itemView);

            foodTitleView = (TextView)itemView.findViewById(R.id.foodTitle);
            foodContentsView = (TextView)itemView.findViewById(R.id.foodContents);
            foodImageView = (ImageView)itemView.findViewById(R.id.foodImage);
            foodCheckView = (CheckBox) itemView.findViewById(R.id.foodCheck);
        }
    }
}
