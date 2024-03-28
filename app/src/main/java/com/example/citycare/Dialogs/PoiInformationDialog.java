package com.example.citycare.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.citycare.LandingPage;
import com.example.citycare.R;

public class PoiInformationDialog extends Dialog{
    Context context;
    FragmentDialog fragmentDialog;
    Activity landingpage;

    public PoiInformationDialog(Context context, Activity landingPage, FragmentDialog fragmentDialog) {
        super(context);
        this.context = context;
        this.fragmentDialog = fragmentDialog;
        this.landingpage = landingPage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //poiInformationDialog = new Dialog(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.poi_informations);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        ConstraintLayout reportButton = findViewById(R.id.reportButton);
        reportButton.setOnClickListener(v-> {
            showFragmentDialog();
        });

    }

    private void showFragmentDialog(){
        Window window = fragmentDialog.getWindow();
        assert window != null;
        window.setGravity(Gravity.TOP);
        window.setDimAmount(0.0f);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dismiss();
        landingpage.findViewById(R.id.dimm).setVisibility(View.VISIBLE);
        fragmentDialog.show();
    }

}
