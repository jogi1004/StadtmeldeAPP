package com.example.citycare.Dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.citycare.FAB.MyFloatingActionButtons;
import com.example.citycare.LandingPage;
import com.example.citycare.R;

import java.util.List;

public class PoiInformationDialog extends Dialog{
    FragmentDialog fragmentDialog;
    Activity landingpage;
    Context context;

    public PoiInformationDialog(Context context, Activity landingPage) {
        super(context);
        this.context = context;
        this.landingpage = landingPage;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentDialog = new FragmentDialog(context, landingpage);

        new Dialog(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.poi_informations);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        ConstraintLayout reportButton = findViewById(R.id.reportButton);
        reportButton.setOnClickListener(v-> {
            fragmentDialog.showFragmentDialog();
            dismiss();
            landingpage.findViewById(R.id.dimm).setVisibility(View.VISIBLE);
        });

    }


    @SuppressLint("SetTextI18n")
    public void fill(List<Address> addresses) {

        TextView adress = findViewById(R.id.adress);
        Log.d("Doch?", "EHM: " + adress.getText());
        TextView adressInfos = findViewById(R.id.adressInfos);
        TextView koords = findViewById(R.id.koords);

        String[] address = addresses.get(0).getAddressLine(0).split(", ");
        adress.setText(address[0]);
        if(addresses.get(0).getSubLocality() == null){
            adressInfos.setText(address[0] + ", " + addresses.get(0).getLocality());
        }else {
            adressInfos.setText(address[0] + ", " + addresses.get(0).getSubLocality());
        }
        koords.setText(addresses.get(0).getLatitude() + ", " + addresses.get(0).getLongitude());

        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        show();
    }
}
