package com.example.citycare.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.citycare.FAB.MyFloatingActionButtons;
import com.example.citycare.R;

public class FragmentDialog extends Dialog {

    Context context;
    FrameLayout dimm;

    public FragmentDialog(@NonNull Context context, Activity landingPage) {
        super(context);
        this.context = context;
        this.dimm = landingPage.findViewById(R.id.dimm);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.report_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setOnDismissListener(v->{
            dimm.setVisibility(View.GONE);
        });
    }
}
