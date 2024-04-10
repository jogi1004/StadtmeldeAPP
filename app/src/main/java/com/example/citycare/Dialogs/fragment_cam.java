package com.example.citycare.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.citycare.R;
import com.example.citycare.model.ReportModel;

public class fragment_cam extends Fragment implements View.OnClickListener {

    private ReportModel report;

    public fragment_cam(ReportModel report) {
        this.report = report;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_picture, container, false);

        ImageButton nextFragment = rootView.findViewById(R.id.nextFragment);
        nextFragment.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        //null später ersetzen durch Bild
        report.setImage(null);

        fragment_report reportF = new fragment_report(report);

        final FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.flFragment, reportF);
        transaction.commitNow();
    }
}