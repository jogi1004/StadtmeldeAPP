package com.example.citycare.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.citycare.LandingPage;
import com.example.citycare.R;
import com.example.citycare.WelcomePage;
import com.example.citycare.util.APIHelper;

public class ProfilDialog extends Dialog {

    Context context;
    FrameLayout dimm;
    APIHelper apiHelper;

    public ProfilDialog(@NonNull Context context, Activity landingPage) {
        super(context);
        this.context = context;
        this.dimm = findViewById(R.id.dimm);
        apiHelper = new APIHelper(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_profile);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            apiHelper.setLoggedIn(false);
            apiHelper.updateSharedPreferences();
            Intent i = new Intent(context, WelcomePage.class);
            context.startActivity(i);
        });
    }


}
