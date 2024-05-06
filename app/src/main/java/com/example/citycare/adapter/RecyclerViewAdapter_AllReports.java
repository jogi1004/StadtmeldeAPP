package com.example.citycare.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.citycare.R;
import com.example.citycare.model.ReportModel;
import com.example.citycare.util.RecyclerViewInterface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter_AllReports extends RecyclerView.Adapter<RecyclerViewAdapter_AllReports.MyViewHolder>{

    private RecyclerViewInterface recyclerViewInterface;
    private Context context;
    private List<ReportModel> allReports;
    public RecyclerViewAdapter_AllReports(Context context, List<ReportModel> allReports){
        this.context = context;
        this.allReports = allReports;

    }
    @NonNull
    @Override
    public RecyclerViewAdapter_AllReports.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_recycler_report, parent, false);
        return new RecyclerViewAdapter_AllReports.MyViewHolder(view, recyclerViewInterface);
    }

    public void updateList(List<ReportModel> reports){
        Log.d("updateallReports", String.valueOf(reports.size()));
        this.allReports = reports;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter_AllReports.MyViewHolder holder, int position) {
        if (allReports.get(position).getTitle()!=null){
            holder.reportName.setText(allReports.get(position).getTitle());
        } else {
            holder.reportName.setText(allReports.get(position).getSubCategory());
        }

        if (allReports.get(position).getTimestamp()!=null){
            holder.reportDate.setText(allReports.get(position).getTimestamp());
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MMMM yyyy, HH:mm", Locale.getDefault());
            Date date = new Date();
            String formattedDate = sdf.format(date);
            holder.reportDate.setText(formattedDate);
        }

        if (allReports.get(position).getImage()!=null){
            holder.image.setImageBitmap(allReports.get(position).getImage());
        }else {
            holder.image.setImageResource(R.drawable.png_dummy);
        }
//        if(allReports.get(position).getIconId() != -1){
//            holder.icon.setImageBitmap(allReports.get(position).getIcon());

        if(allReports.get(position).getMainCategoryModel()!=null){
            Log.d("icon", allReports.get(position).toString());
            holder.icon.setImageBitmap(allReports.get(position).getMainCategoryModel().getIcon().getIcon());
        } else {
            holder.icon.setImageResource(android.R.drawable.ic_dialog_alert);
        }
    }

    @Override
    public int getItemCount() {
        return allReports.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image, icon;
        TextView reportName, reportDate;


        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            image = itemView.findViewById(R.id.reportImage);
            reportName = itemView.findViewById(R.id.reportName);
            reportDate = itemView.findViewById(R.id.reportDate);
            icon = itemView.findViewById(R.id.categoryIcon);
            itemView.setOnClickListener(v -> {
                if (recyclerViewInterface != null) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onItemClick(pos);
                    }

                }
            });
        }
    }

    public void setRecyclerViewInterface(RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
    }
}
