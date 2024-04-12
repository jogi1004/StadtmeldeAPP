package com.example.citycare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.citycare.R;
import com.example.citycare.model.ReportModel;

import java.util.ArrayList;

public class RecyclerViewAdapter_AllReports extends RecyclerView.Adapter<RecyclerViewAdapter_AllReports.MyViewHolder>{

    Context context;
    ArrayList<ReportModel> allReports;
    public RecyclerViewAdapter_AllReports(Context context, ArrayList<ReportModel> allReports){
        this.context = context;
        this.allReports = allReports;
    }
    @NonNull
    @Override
    public RecyclerViewAdapter_AllReports.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_recycler_report, parent, false);
        return new RecyclerViewAdapter_AllReports.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter_AllReports.MyViewHolder holder, int position) {
        holder.reportName.setText(allReports.get(position).getSubCategory());
        holder.reportDate.setText(allReports.get(position).getTimestamp());
        //holder.image.setImageResource(R.drawable.png_placeholder);
    }

    @Override
    public int getItemCount() {
        return allReports.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView reportName,reportDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.reportImage);
            reportName = itemView.findViewById(R.id.reportName);
            reportDate = itemView.findViewById(R.id.reportDate);
        }
    }
}
