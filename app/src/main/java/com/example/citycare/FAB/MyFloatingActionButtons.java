package com.example.citycare.FAB;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.citycare.Dialogs.PoiInformationDialog;
import com.example.citycare.Dialogs.ProfilDialog;
import com.example.citycare.Dialogs.ReportDialogPage;
import com.example.citycare.Dialogs.SearchDialog;
import com.example.citycare.Dialogs.SettingDialog;
import com.example.citycare.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyFloatingActionButtons {
    Context context;
    PoiInformationDialog poiInformationDialog;
    private FloatingActionButton menuFAB, profilFAB, searchFAB, allReportsFAB, settingsFAB;
    private Boolean areFabsVisible;
    private ProfilDialog profilDialog;
    private ReportDialogPage allReportsDialog;
    private SettingDialog settingDialog;
    private SearchDialog searchDialog;
    private int dialogheight;


    public MyFloatingActionButtons(Context context, Activity landingPage, Boolean areFabsVisible, ProfilDialog profilDialog, SettingDialog settingDialog, ReportDialogPage allReportsDialog, PoiInformationDialog poiInformationDialog, SearchDialog searchDialog) {
        this.context = context;
        this.menuFAB = landingPage.findViewById(R.id.menu);
        this.profilFAB = landingPage.findViewById(R.id.profil);
        this.allReportsFAB = landingPage.findViewById(R.id.allReports);
        this.settingsFAB = landingPage.findViewById(R.id.setting);
        this.searchFAB = landingPage.findViewById(R.id.searchFAB);
        this.areFabsVisible = areFabsVisible;
        this.profilDialog = profilDialog;
        this.settingDialog = settingDialog;
        this.allReportsDialog = allReportsDialog;
        this.poiInformationDialog = poiInformationDialog;
        this.searchDialog = searchDialog;


        DisplayMetrics displayMetrics = new DisplayMetrics();
        landingPage.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        dialogheight = (int) (displayMetrics.heightPixels*0.7);
        searchFAB.setVisibility(View.GONE);
        profilFAB.setVisibility(View.GONE);
        allReportsFAB.setVisibility(View.GONE);
        settingsFAB.setVisibility(View.GONE);

        menuFAB.setOnClickListener(v->toggleFABMenu());
        profilFAB.setOnClickListener(v->{
            showProfileDialog();
            landingPage.findViewById(R.id.dimm).setVisibility(View.VISIBLE);
            searchFAB.setVisibility(View.GONE);
            allReportsFAB.setVisibility(View.GONE);
            settingsFAB.setVisibility(View.GONE);
        });
        allReportsFAB.setOnClickListener(v ->{
            showAllReportsDialog();
            landingPage.findViewById(R.id.dimm).setVisibility(View.VISIBLE);
            searchFAB.setVisibility(View.GONE);
            profilFAB.setVisibility(View.GONE);
            settingsFAB.setVisibility(View.GONE);
                });
        settingsFAB.setOnClickListener(v->{
            showSettingDialog();
            landingPage.findViewById(R.id.dimm).setVisibility(View.VISIBLE);
            searchFAB.setVisibility(View.GONE);
            allReportsFAB.setVisibility(View.GONE);
            profilFAB.setVisibility(View.GONE);
        });
        searchFAB.setOnClickListener(v->{
            showSearchDialog();
            landingPage.findViewById(R.id.dimm).setVisibility(View.VISIBLE);
            allReportsFAB.setVisibility(View.GONE);
            profilFAB.setVisibility(View.GONE);
            settingsFAB.setVisibility(View.GONE);
        });

        allReportsDialog.getDetailView().setOnDismissListener(v->{
            allReportsFAB.setVisibility(View.GONE);
            setAreFabsVisible(false);
            allReportsFAB.setVisibility(View.VISIBLE);
        });
        allReportsDialog.setOnDismissListener(v->{
            allReportsFAB.setVisibility(View.GONE);
            setAreFabsVisible(false);
            landingPage.findViewById(R.id.dimm).setVisibility(View.GONE);
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
        searchDialog.setOnDismissListener(v->{
            searchFAB.setVisibility(View.GONE);
            setAreFabsVisible(false);
            landingPage.findViewById(R.id.dimm).setVisibility(View.GONE);
        });
        poiInformationDialog.setOnDismissListener(v->{
            hideFABS();
        });


    }
    private void hideFABS(){
        searchFAB.setVisibility(View.GONE);
        allReportsFAB.setVisibility(View.GONE);
        profilFAB.setVisibility(View.GONE);
        settingsFAB.setVisibility(View.GONE);
        areFabsVisible = false;
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

    private void showSearchDialog(){
        Window window = searchDialog.getWindow();
        window.setGravity(Gravity.TOP);
        window.setDimAmount(0.0f);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WRAP_CONTENT);
        searchDialog.show();
    }


    private void toggleFABMenu(){
        if(!areFabsVisible){
            //show
            profilFAB.show();
            searchFAB.show();
            allReportsFAB.show();
            settingsFAB.show();

            areFabsVisible = true;
        }else{
            //hide
            profilFAB.hide();
            searchFAB.hide();
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

    public FloatingActionButton getSearchFAB() {
        return searchFAB;
    }

    public void setSearchFAB(FloatingActionButton searchFAB) {
        this.searchFAB = searchFAB;
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
