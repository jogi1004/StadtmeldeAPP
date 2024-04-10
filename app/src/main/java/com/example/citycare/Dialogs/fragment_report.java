package com.example.citycare.Dialogs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.citycare.R;
import com.example.citycare.model.ReportModel;

public class fragment_report extends Fragment{

    TextView category, subCategory, koords;
    EditText description;
    ReportModel report;

    public fragment_report(ReportModel report) {
        this.report = report;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_report, container, false);

        category = rootView.findViewById(R.id.category);
        category.setText(report.getMainCategory());

        subCategory = rootView.findViewById(R.id.subCategory);
        subCategory.setText(report.getSubCategory());

        koords = rootView.findViewById(R.id.koords);
        koords.setText(report.getLatitude() + ", " + report.getLongitude());

        description = rootView.findViewById(R.id.endDescription);
        description.setText(report.getDescription());

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}