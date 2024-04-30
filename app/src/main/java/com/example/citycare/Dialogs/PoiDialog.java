package com.example.citycare.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.citycare.R;
import com.example.citycare.model.ReportModel;

import java.io.IOException;
import java.util.List;

public class PoiDialog extends Dialog {

    private ImageView imageView;
    private TextView title;
    private Context context;
    private int dialogheight;
    private ReportModel m;
    public PoiDialog(@NonNull Context context, ReportModel m) {
        super(context);
        this.context = context;
        this.m = m;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_poi_onclick);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        imageView =  findViewById(R.id.poi_report_pic);
        title = findViewById(R.id.poiTitle);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        dialogheight = (int) (displayMetrics.heightPixels * 0.4);

        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, dialogheight);
        getWindow().setDimAmount(0.0f);



        if (m.getImage()!=null){
            imageView.setImageBitmap(m.getImage());
        }
        title.setText(m.getTitle());

    }
}
