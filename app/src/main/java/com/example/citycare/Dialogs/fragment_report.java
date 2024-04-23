package com.example.citycare.Dialogs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.citycare.LandingPage;
import com.example.citycare.R;
import com.example.citycare.model.ReportModel;
import com.example.citycare.util.APIHelper;

import org.json.JSONException;

public class fragment_report extends Fragment implements View.OnClickListener {

    private TextView category, subCategory, koords;
    private ImageView reportPic;
    private EditText description;
    private ReportModel report;
    private ConstraintLayout sendReport;
    private View rootView;

    public fragment_report(ReportModel report) {
        this.report = report;
    }

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_report, container, false);
        category = rootView.findViewById(R.id.category);
        category.setText(report.getMainCategory());

        subCategory = rootView.findViewById(R.id.subCategory);
        if(report.getSubCategory().equals("Sonstiges")){
            subCategory.setText(report.getTitle());
        }else{
            subCategory.setText(report.getSubCategory());
        }

        koords = rootView.findViewById(R.id.koords);
        koords.setText(report.getLocationName());

        description = rootView.findViewById(R.id.endDescription);
        description.setText(report.getDescription());

        sendReport = rootView.findViewById(R.id.sendReport);
        sendReport.setOnClickListener(this);

        reportPic = rootView.findViewById(R.id.camReport);
        if (report.getImage()!=null){
            reportPic.setImageBitmap(report.getImage());
        } else {
            reportPic.setImageResource(R.drawable.png_placeholder);
        }


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        APIHelper apiHelper = APIHelper.getInstance(this.getContext());
        try {
            apiHelper.postReport(report);
            FragmentDialog fragmentDialog = (FragmentDialog) getActivity().getSupportFragmentManager().findFragmentByTag("FragmentDialog");
            if (fragmentDialog != null) {
                fragmentDialog.dismiss();
            }
            LandingPage.getCamUtil().setBitmap(null);
            Toast toast = new Toast(rootView.getContext());
            toast.setText("Meldung erfolgreich abgeschickt!");
            toast.show();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
}