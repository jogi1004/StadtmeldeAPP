package com.example.citycare.Dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

public class fragment_damagetitle extends Fragment implements View.OnClickListener {

    public TextView title;
    private EditText description;
    private ReportModel report;


    public fragment_damagetitle(ReportModel report) {
        this.report = report;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_title, container, false);

        title = rootView.findViewById(R.id.title);
        description = rootView.findViewById(R.id.newDescription);

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
        report.setDescription(String.valueOf(description.getText()));

        fragment_cam camF = new fragment_cam(report);

        final FragmentManager fragmentManager = getParentFragmentManager();

        FragmentDialog fragmentDialog = (FragmentDialog) getParentFragmentManager().findFragmentByTag("FragmentDialog");
        if (fragmentDialog != null) {
            Log.d("Fragment", "Fragment != null");
            fragmentDialog.dismiss();
        }else {
            Log.d("Fragment", "Fragment == null");
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.flFragment, camF);
        transaction.commitNow();
    }
}