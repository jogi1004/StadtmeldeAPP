package com.example.citycare.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.citycare.LandingPage;
import com.example.citycare.R;
import com.example.citycare.model.ReportModel;
import com.example.citycare.adapter.RecyclerViewAdapter_AllReports;;
import com.example.citycare.util.RecyclerViewInterface;
import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView for list of reports in users neighbourhood
 */
public class ReportDialogPage extends Dialog implements RecyclerViewInterface {

    private Context context;
    private FrameLayout dim;
    private List<ReportModel> allReports = new ArrayList<>();
    private DialogReportDetailView detailView;
    private int dialogheight;
    public static RecyclerViewAdapter_AllReports recyclerAdapter;
    RecyclerView recyclerView;
    public ReportDialogPage(@NonNull Context context, Activity landingPage, RecyclerViewAdapter_AllReports adapterAllReports) {
        super(context);
        this.context = context;
        this.dim = findViewById(R.id.dimm);
        detailView = new DialogReportDetailView(context);
        recyclerAdapter = adapterAllReports;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_reports);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initAllReports();
        initAdapter();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        dialogheight = (int) (displayMetrics.heightPixels * 0.7);

    }
    protected void initAdapter(){
        recyclerView = findViewById(R.id.reportsrecyclerview);
        if(recyclerAdapter != null) {
            recyclerView.setAdapter(recyclerAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerAdapter = new RecyclerViewAdapter_AllReports(context, allReports, this);
            recyclerView.setAdapter(recyclerAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }
    }

    private void initAllReports() {
        allReports = LandingPage.getAllReportsList();

    }

    @Override
    public void onItemClick(int position) {
        ReportModel clickedReport = allReports.get(position);
        detailView.setReportModel(clickedReport);
        detailView.show();
        detailView.setData();
        Window window = detailView.getWindow();
        window.setGravity(Gravity.TOP);
        window.setDimAmount(0.0f);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, dialogheight);

    }

    public DialogReportDetailView getDetailView() {
        return detailView;
    }
}
