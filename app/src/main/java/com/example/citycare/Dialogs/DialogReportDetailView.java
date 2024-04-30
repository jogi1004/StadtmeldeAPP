package com.example.citycare.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.citycare.R;
import com.example.citycare.model.ReportModel;

/**
 * gets started when user clicks on report in recycler view and shows the report in
 * detail with location, user who reported,report title,image and description
 */
public class DialogReportDetailView extends Dialog {
    private TextView headline,descriptionReport,cityTextView;
    private ImageView imageReport;
    private Bitmap imageBitmap;
    private ReportModel reportModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_report_detail_view);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        headline = findViewById(R.id.headlineReport);
        imageReport = findViewById(R.id.reportImage);
        descriptionReport = findViewById(R.id.descriptionDetail);
        cityTextView = findViewById(R.id.city);

    }

    public DialogReportDetailView(@NonNull Context context) {
        super(context);
    }

    public void setReportModel(ReportModel reportModel) {
        this.reportModel=reportModel;

    }

    /**
     * fills DetailView with data
     */
    public void setData(){
        headline.setText(reportModel.getTitle());
        if(reportModel.getImage() != null){
            imageReport.setImageBitmap(reportModel.getImage());
        }else {
            imageReport.setImageResource(R.drawable.png_dummy);
        }
        Log.d("Description",reportModel.getDescription() + "");
        descriptionReport.setText(reportModel.getDescription());
        cityTextView.setText(reportModel.getLocationName());
    }
}
