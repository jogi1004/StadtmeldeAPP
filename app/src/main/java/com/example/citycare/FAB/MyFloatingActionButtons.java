package com.example.citycare.FAB;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.example.citycare.Dialogs.ProfilDialog;
import com.example.citycare.Dialogs.ReportDialogPage;
import com.example.citycare.Dialogs.SettingDialog;
import com.example.citycare.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyFloatingActionButtons {
    Context context;
    private FloatingActionButton menuFAB;
    private FloatingActionButton profilFAB;
    private FloatingActionButton addFAB;
    private FloatingActionButton allReportsFAB;
    private FloatingActionButton settingsFAB;
    private Boolean areFabsVisible;
    private ProfilDialog profilDialog;
    private ReportDialogPage allReportsDialog;
    private SettingDialog settingDialog;
    private int dialogheight;

    public MyFloatingActionButtons(Context context, Activity landingPage, Boolean areFabsVisible, ProfilDialog profilDialog, SettingDialog settingDialog, ReportDialogPage allReportsDialog) {
        this.context = context;
        this.menuFAB = landingPage.findViewById(R.id.menu);
        this.profilFAB = landingPage.findViewById(R.id.profil);
        this.addFAB = landingPage.findViewById(R.id.addReports);;
        this.allReportsFAB = landingPage.findViewById(R.id.allReports);;
        this.settingsFAB = landingPage.findViewById(R.id.setting);;
        this.areFabsVisible = areFabsVisible;
        this.profilDialog = profilDialog;
        this.settingDialog = settingDialog;
        this.allReportsDialog = allReportsDialog;


        DisplayMetrics displayMetrics = new DisplayMetrics();
        landingPage.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        dialogheight = (int) (displayMetrics.heightPixels*0.7);
        addFAB.setVisibility(View.GONE);
        profilFAB.setVisibility(View.GONE);
        allReportsFAB.setVisibility(View.GONE);
        settingsFAB.setVisibility(View.GONE);

        menuFAB.setOnClickListener(v->toggleFABMenu());
        profilFAB.setOnClickListener(v->{
            showProfileDialog();
            landingPage.findViewById(R.id.dimm).setVisibility(View.VISIBLE);
            addFAB.setVisibility(View.GONE);
            allReportsFAB.setVisibility(View.GONE);
            settingsFAB.setVisibility(View.GONE);
        });
        allReportsFAB.setOnClickListener(v ->{
            showAllReportsDialog();
            landingPage.findViewById(R.id.dimm).setVisibility(View.VISIBLE);
            addFAB.setVisibility(View.GONE);
            profilFAB.setVisibility(View.GONE);
            settingsFAB.setVisibility(View.GONE);
                });

        allReportsDialog.setOnDismissListener(v->{
            allReportsFAB.setVisibility(View.GONE);
            setAreFabsVisible(false);
            landingPage.findViewById(R.id.dimm).setVisibility(View.GONE);
        });

        settingsFAB.setOnClickListener(v->{
            showSettingDialog();
            landingPage.findViewById(R.id.dimm).setVisibility(View.VISIBLE);
            addFAB.setVisibility(View.GONE);
            allReportsFAB.setVisibility(View.GONE);
            profilFAB.setVisibility(View.GONE);
        });

        profilDialog.setOnDismissListener(v->{
            profilFAB.setVisibility(View.GONE);
            setAreFabsVisible(false);
            landingPage.findViewById(R.id.dimm).setVisibility(View.GONE);
        });

        settingDialog.setOnDismissListener(v->{
            settingsFAB.setVisibility(View.GONE);
            setAreFabsVisible(false);
            landingPage.findViewById(R.id.dimm).setVisibility(View.GONE);
        });


    }
    private void showAllReportsDialog() {
        Window window = allReportsDialog.getWindow();
        assert window != null;
        window.setGravity(Gravity.TOP);
        window.setDimAmount(0.0f);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,dialogheight);
        allReportsDialog.show();
    }

    private void showProfileDialog(){
        Window window = profilDialog.getWindow();
        window.setGravity(Gravity.TOP);
        window.setDimAmount(0.0f);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, dialogheight);
        profilDialog.show();
    }

    private void showSettingDialog(){

        Window window = settingDialog.getWindow();
        window.setGravity(Gravity.TOP);
        window.setDimAmount(0.0f);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, dialogheight);
        settingDialog.show();
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

    public FloatingActionButton getMenuFAB() {
        return menuFAB;
    }

    public void setMenuFAB(FloatingActionButton menuFAB) {
        this.menuFAB = menuFAB;
    }

    public FloatingActionButton getProfilFAB() {
        return profilFAB;
    }

    public void setProfilFAB(FloatingActionButton profilFAB) {
        this.profilFAB = profilFAB;
    }

    public FloatingActionButton getAddFAB() {
        return addFAB;
    }

    public void setAddFAB(FloatingActionButton addFAB) {
        this.addFAB = addFAB;
    }

    public FloatingActionButton getAllReportsFAB() {
        return allReportsFAB;
    }

    public void setAllReportsFAB(FloatingActionButton allReportsFAB) {
        this.allReportsFAB = allReportsFAB;
    }

    public FloatingActionButton getSettingsFAB() {
        return settingsFAB;
    }

    public void setSettingsFAB(FloatingActionButton settingsFAB) {
        this.settingsFAB = settingsFAB;
    }

    public Boolean getAreFabsVisible() {
        return areFabsVisible;
    }

    public void setAreFabsVisible(Boolean areFabsVisible) {
        this.areFabsVisible = areFabsVisible;
    }
}
