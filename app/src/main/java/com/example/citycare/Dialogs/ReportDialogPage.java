package com.example.citycare.Dialogs;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.citycare.LandingPage;
import com.example.citycare.R;
import com.example.citycare.model.ReportModel;
import com.example.citycare.adapter.RecyclerViewAdapter_AllReports;
import com.example.citycare.util.APIHelper;
import com.example.citycare.util.RecyclerViewInterface;

import java.util.ArrayList;

public class ReportDialogPage extends Dialog implements RecyclerViewInterface {

    Context context;
    FrameLayout dim;
    ArrayList<ReportModel> allReports = new ArrayList<>();
    public ReportDialogPage(@NonNull Context context) {
        super(context);
        this.context = context;
        this.dim = findViewById(R.id.dimm);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_reports);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initAllReports();
        RecyclerView recyclerView = findViewById(R.id.reportsrecyclerview);
        RecyclerViewAdapter_AllReports recyclerAdapter = new RecyclerViewAdapter_AllReports(context, allReports, this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void initAllReports() {
        allReports = LandingPage.getAllReportsList();

    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(context, "Item " + position + " angeklickt", Toast.LENGTH_SHORT).show();
        Intent Intent = new Intent(context,DialogReportDetailView.class);
    }
}
