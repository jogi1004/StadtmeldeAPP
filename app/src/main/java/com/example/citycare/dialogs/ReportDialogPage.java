package com.example.citycare.dialogs;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import com.example.citycare.R;


public class ReportDialogPage extends Dialog {

    public ReportDialogPage(@NonNull Context context) {
        super(context);
        setContentView(R.layout.report_dialogpage);
    }
}
