package com.example.citycare.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.example.citycare.R;
import com.example.citycare.WelcomePage;
import com.example.citycare.util.APIHelper;
import com.example.citycare.util.CamUtil;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilDialog extends Dialog {

    private Context context;
    private FrameLayout dimm;
    private APIHelper apiHelper;
    private CircleImageView picture;
    public Activity landingPage;
    private CamUtil camUtil;
    private TextView username;
    public ProfilDialog(@NonNull Context context, Activity landingPage, CamUtil camUtil) {
        super(context);
        this.context = context;
        this.dimm = findViewById(R.id.dimm);
        this.landingPage = landingPage;
        apiHelper = APIHelper.getInstance(context);
        this.camUtil= camUtil;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_profile);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        username = findViewById(R.id.name);
        username.setText(apiHelper.getCurrentUser().getUsername());


        ImageView logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {

            SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent i = new Intent(context, WelcomePage.class);
            context.startActivity(i);
        });

        picture = findViewById(R.id.circleImageView);
        picture.setImageBitmap(apiHelper.getCurrentUser().getProfilePicture());
        picture.setOnClickListener(v -> camUtil.initDialog(2,1));
    }



    public CircleImageView getPicture() {
        return picture;
    }
    public File getImageFile() {
        return camUtil.getImageFile();
    }
}
