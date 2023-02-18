package com.noahliu.horizontalrecyclerviewwithindicatorexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public ArrayList<TanksData> data;
    private RecyclerViewIndicator indicator;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle,tvDepiction;
        ImageView igTank;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.textView_title);
            tvDepiction = itemView.findViewById(R.id.textView_depiction);
            igTank = itemView.findViewById(R.id.imageView_tank);

        }
    }

    public MyAdapter(ArrayList<TanksData> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TanksData tanksData = data.get(position);
        holder.tvTitle.setText(tanksData.getTitle());
        holder.tvDepiction.setText(tanksData.getDepiction());
        holder.igTank.setImageResource(tanksData.getImage());
    }
    //綁定標籤點點列
    public void setIndicator(RecyclerViewIndicator indicator,RecyclerView recyclerView){
        this.indicator = indicator;
        this.indicator.setWithRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
