package com.example.citycare.Dialogs;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.FrameLayout;

import com.example.citycare.R;
import com.example.citycare.model.ReportModel;
import com.example.citycare.adapter.RecyclerViewAdapter_AllReports;

import java.util.ArrayList;

public class ReportDialogPage extends Dialog {

    Context context;
    FrameLayout dim;
    ArrayList<ReportModel> allReports = new ArrayList<>();
    String[] reportNames = {"Meldung 1", "Meldung 2", "Meldung 3", "Meldung 4", "Meldung 5"};
    String[] reportDates = {"22.03.2023 | 13:23Uhr", "23.09.2016 | 15:49Uhr", "06.11.2003 | 23:23Uhr", "30.02.2021 | 12:21Uhr", "01.10.2004 | 17:48Uhr"};
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
        RecyclerViewAdapter_AllReports recyclerAdapter = new RecyclerViewAdapter_AllReports(context, allReports);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void initAllReports() {
        for (int i = 0; i < reportNames.length; i++){
            allReports.add(new ReportModel(reportNames[i], reportDates[i], null));
        }
    }
}
