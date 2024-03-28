package com.example.citycare.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.example.citycare.R;

public class PoiInformationDialog extends Dialog{
    Dialog poiInformationDialog;
    Context context;

    public PoiInformationDialog(Context context, Activity landingPage) {
        super(context);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        poiInformationDialog = new Dialog(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.poi_informations);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


    }
}
