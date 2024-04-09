package com.example.citycare.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.citycare.R;
import com.example.citycare.model.MainCategoryModel;
import com.example.citycare.model.SubCategoryModel;
import com.example.citycare.util.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter_SubCategories extends RecyclerView.Adapter<RecyclerViewAdapter_SubCategories.MyViewHolder>{

    private Context context;
    private List<SubCategoryModel> subCategories;
    private OnItemClickListener mListener;

    public RecyclerViewAdapter_SubCategories(Context context, List<SubCategoryModel> subCategories) {
        this.context = context;
        this.subCategories = subCategories;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter_SubCategories.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_subcategory, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter_SubCategories.MyViewHolder holder, int position) {
        SubCategoryModel daten = subCategories.get(position);
        holder.title.setText(daten.getTitle());

        holder.title.setOnClickListener(v -> {
            if(mListener != null){
                mListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subCategories.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.subCategoryTitle);
        }

    }
}
