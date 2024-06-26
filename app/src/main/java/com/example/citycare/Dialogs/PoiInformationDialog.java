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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.citycare.LandingPage;
import com.example.citycare.R;
import com.example.citycare.model.ReportModel;

import java.util.List;


public class PoiInformationDialog extends Dialog{
    
    Activity landingpage;
    Context context;
    FragmentManager supportFragmentManager;
    double lat, lon;
    String locationName;
    ImageView gifImageView;
    ConstraintLayout reportButton;

    public PoiInformationDialog(Context context, Activity landingPage, FragmentManager supportFragmentManager) {
        super(context);
        this.context = context;
        this.landingpage = landingPage;
        this.supportFragmentManager = supportFragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Dialog(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_poi_information);


        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        gifImageView = findViewById(R.id.gif);
        Glide.with(context).asGif().load(R.drawable.gif_punkte_laden).into(gifImageView);

        reportButton = findViewById(R.id.reportButton);
        reportButton.setOnClickListener(v-> {
                FragmentDialog dialog = new FragmentDialog();
                FrameLayout dimm = landingpage.findViewById(R.id.dimm);
                dialog.showFragmentDialog(supportFragmentManager, dimm, lat, lon, locationName);
                dimm.setVisibility(View.VISIBLE);
                dismiss();
        });

    }

    @SuppressLint("SetTextI18n")
    public void fill(List<Address> addresses) {

        TextView adress = findViewById(R.id.adress);
        TextView adressInfos = findViewById(R.id.adressInfos);
        TextView koords = findViewById(R.id.koords);

        locationName = addresses.get(0).getLocality();
        Log.d("City", locationName);

        String[] address = addresses.get(0).getAddressLine(0).split(", ");
        adress.setText(address[0]);
        if(addresses.get(0).getSubLocality() == null){
            adressInfos.setText(address[0] + ", " + addresses.get(0).getLocality());
        }else {
            adressInfos.setText(address[0] + ", " + addresses.get(0).getSubLocality());
        }
        lat = addresses.get(0).getLatitude();
        lon = addresses.get(0).getLongitude();

        koords.setText(lat + ", " + lon);

        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        show();
    }

    public void setGifVisibility(int visibility) {
        gifImageView.setVisibility(visibility);
    }

    public void setButtonVisibility(int visibility) {
        reportButton.setVisibility(visibility);
    }
}
