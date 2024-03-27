package com.example.citycare;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LandingPage extends AppCompatActivity {

    private Dialog profileDialog;
    private FloatingActionButton profilFAB, addFAB, allReportsFAB, settingsFAB;
    private Boolean areFabsVisible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.landing_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initFABMenu();
        initProfilDialog();
    }


    private void initProfilDialog(){
        profileDialog = new Dialog(this);
        profileDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        profileDialog.setContentView(R.layout.profile_dialog);
        profileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Window window = profileDialog.getWindow();
        window.setGravity(Gravity.TOP);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void initFABMenu(){
        profilFAB = findViewById(R.id.profil);
        addFAB = findViewById(R.id.addReports);
        allReportsFAB = findViewById(R.id.allReports);
        settingsFAB = findViewById(R.id.setting);
        FloatingActionButton menuFAB = findViewById(R.id.menu);


        //Hide Buttons
        addFAB.setVisibility(View.GONE);
        profilFAB.setVisibility(View.GONE);
        allReportsFAB.setVisibility(View.GONE);
        settingsFAB.setVisibility(View.GONE);
        areFabsVisible = false;

        menuFAB.setOnClickListener(v->toggleFABMenu());
        profilFAB.setOnClickListener(v -> {
            profileDialog.show();
            //Hide all other FAB Buttons
            addFAB.setVisibility(View.GONE);
            allReportsFAB.setVisibility(View.GONE);
            settingsFAB.setVisibility(View.GONE);

        });
    }
    private void toggleFABMenu(){
        if(!areFabsVisible){
            //show
            profilFAB.show();
            addFAB.show();
            allReportsFAB.show();
            settingsFAB.show();

            areFabsVisible = true;
        }else{
            //hide
            profilFAB.hide();
            addFAB.hide();
            allReportsFAB.hide();
            settingsFAB.hide();

            areFabsVisible = false;
        }
    }
}