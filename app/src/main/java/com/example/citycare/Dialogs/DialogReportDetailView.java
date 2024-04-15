package com.example.citycare.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.citycare.R;
import com.example.citycare.model.SubCategoryModel;

public class DialogReportDetailView extends Dialog {
    TextView headline,descriptionReport,coordinates;
    ImageView imageReport;
    Bitmap imageBitmap;

    public DialogReportDetailView(@NonNull Context context,String subCategory, String image, String description, double longitude, double latitude, String timestamp) {
        super(context);
        setContentView(R.layout.dialog_report_detail_view);
        headline = findViewById(R.id.headlineReport);
        imageReport = findViewById(R.id.reportImage);
        descriptionReport = findViewById(R.id.descriptionReportDetailView);
        coordinates = findViewById(R.id.coordinates);

        headline.setText(subCategory);
        if(image != null){
            byte[] decodedBytes = Base64.decode(image, Base64.DEFAULT);
            imageBitmap = BitmapFactory.decodeByteArray(decodedBytes,0, decodedBytes.length);
            imageReport.setImageBitmap(imageBitmap);
        }
        descriptionReport.setText(description);
        String coordinatesReport = longitude + ", " + latitude;
        coordinates.setText(coordinatesReport);

    }
}
