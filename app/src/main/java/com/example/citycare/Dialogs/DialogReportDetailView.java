package com.example.citycare.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.citycare.R;
import com.example.citycare.model.ReportModel;


public class DialogReportDetailView extends Dialog {
    TextView headline,descriptionReport,cityTextView;
    ImageView imageReport;
    Bitmap imageBitmap;
    String title,image,description,timestamp,city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_report_detail_view);
        headline = findViewById(R.id.headlineReport);
        imageReport = findViewById(R.id.reportImage);
        descriptionReport = findViewById(R.id.descriptionDetail);
        cityTextView = findViewById(R.id.city);
        headline.setText(title);
        if(image != null){
            byte[] decodedBytes = Base64.decode(image, Base64.DEFAULT);
            imageBitmap = BitmapFactory.decodeByteArray(decodedBytes,0, decodedBytes.length);
            imageReport.setImageBitmap(imageBitmap);
        }
        descriptionReport.setText(description);
        cityTextView.setText(city);

    }

    public DialogReportDetailView(@NonNull Context context, ReportModel ClickedReport) {
        super(context);
        this.title = ClickedReport.getTitle();
        this.image = ClickedReport.getImage();
        this.description = ClickedReport.getDescription();
        this.city = ClickedReport.getLocationName(); // klappt nicht ist leer
        this.timestamp = ClickedReport.getTimestamp();

    }

}
