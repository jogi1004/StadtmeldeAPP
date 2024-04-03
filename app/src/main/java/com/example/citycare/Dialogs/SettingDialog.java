package com.example.citycare.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.example.citycare.R;

public class SettingDialog extends Dialog {

    Context context;
    FrameLayout dimm;


    public SettingDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        this.dimm = findViewById(R.id.dimm);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_setting);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
