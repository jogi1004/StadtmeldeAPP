package com.example.citycare.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.citycare.R;
import com.example.citycare.model.DamagetypeModel;

import java.util.ArrayList;

public class RecyclerViewAdapter_Damagetype extends RecyclerView.Adapter<RecyclerViewAdapter_Damagetype.MyViewHolder>{

    private Context context;
    private ArrayList<DamagetypeModel> allDamagetypes;
    private  OnItemClickListener mListener;

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<DamagetypeModel> newData) {
        allDamagetypes = newData;
        notifyDataSetChanged();
    }

    public RecyclerViewAdapter_Damagetype(Context context, ArrayList<DamagetypeModel> allDamagetypes) {
        this.context = context;
        this.allDamagetypes = allDamagetypes;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter_Damagetype.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_damagetype, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter_Damagetype.MyViewHolder holder, int position) {
        DamagetypeModel daten = allDamagetypes.get(position);
        holder.icon.setImageResource(daten.getIcon());
        holder.title.setText(daten.getDamagetype());
        holder.field.setOnClickListener(v -> {
            if(mListener != null){
                mListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allDamagetypes.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView icon;
        private TextView title;

        public ConstraintLayout field;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.name);
            field = itemView.findViewById(R.id.category);
        }

    }
}
