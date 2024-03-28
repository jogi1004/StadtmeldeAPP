package com.example.citycare.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import com.example.citycare.FAB.MyFloatingActionButtons;
import com.example.citycare.LandingPage;
import com.example.citycare.R;

public class ProfilDialog extends Dialog {

    Dialog profileDialog;
    Context context;
    FrameLayout dimm;


    public ProfilDialog(@NonNull Context context, Activity landingPage) {
        super(context);
        this.context = context;
        this.dimm = findViewById(R.id.dimm);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileDialog = new Dialog(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.profile_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


}
