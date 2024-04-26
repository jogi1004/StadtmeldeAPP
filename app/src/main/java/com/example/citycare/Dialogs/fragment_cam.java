package com.example.citycare.Dialogs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.citycare.LandingPage;
import com.example.citycare.R;
import com.example.citycare.model.ReportModel;


public class fragment_cam extends Fragment implements View.OnClickListener {

    private ReportModel report;
    private ImageView imageView;
    private ImageButton backFragment;

    public fragment_cam(ReportModel report) {
        this.report = report;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("Lebenszyklus", "onCreateView() wird aufgerufen");
        View rootView =  inflater.inflate(R.layout.fragment_picture, container, false);

        ImageButton nextFragment = rootView.findViewById(R.id.nextFragment);
        nextFragment.setOnClickListener(this);

        imageView = rootView.findViewById(R.id.cam);
        imageView.setOnClickListener(v->{
            LandingPage.setReportImageView(imageView);
            LandingPage.getCamUtil().initDialog(3,4);
        });

        backFragment = rootView.findViewById(R.id.lastFragment);
        backFragment.setOnClickListener(v->{
            getParentFragmentManager().popBackStack();
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Speichere relevante Daten im Bundle
        outState.putString("reportPic", report.getImage().toString());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("Lebenszyklus", "onViewCreated() wird aufgerufen");
        if(savedInstanceState != null){
            String pic = savedInstanceState.getString("reportPic");
        }
    }

    @Override
    public void onClick(View view) {
        report.setImage(LandingPage.getCamUtil().getBitmap());

        fragment_report reportF = new fragment_report(report);

        final FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.flFragment, reportF);
        transaction.addToBackStack(null);
        transaction.commit();
        Log.d("BackStack", "BackStackEntryCount: " + fragmentManager.getBackStackEntryCount());
    }
}