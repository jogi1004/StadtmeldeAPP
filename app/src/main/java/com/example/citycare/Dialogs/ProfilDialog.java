package com.example.citycare.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.example.citycare.LandingPage;
import com.example.citycare.R;
import com.example.citycare.WelcomePage;
import com.example.citycare.util.APIHelper;
import com.example.citycare.util.CamUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilDialog extends Dialog {

    private Context context;
    private FrameLayout dimm;
    private APIHelper apiHelper;
    private CircleImageView picture;
    private String imagePath;
    public Activity landingPage;
    private CamUtil camUtil;
    public ProfilDialog(@NonNull Context context, Activity landingPage) {
        super(context);
        this.context = context;
        this.dimm = findViewById(R.id.dimm);
        this.landingPage = landingPage;
        apiHelper = APIHelper.getInstance(context);
        camUtil= new CamUtil(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_profile);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
        picture.setOnClickListener(v -> initDialog());
    }
    private void initDialog() {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_input_bild);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageButton gallery = dialog.findViewById(R.id.intentGallery);
        ImageButton cam = dialog.findViewById(R.id.intentCamera);

        gallery.setOnClickListener(v -> {
            Toast toast = new Toast(context);
            toast.setText("Galerie");
            toast.show();

            Intent fotoAuswaehlenIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            landingPage.startActivityForResult(fotoAuswaehlenIntent,2);
            dialog.dismiss();
        });

        cam.setOnClickListener(v -> {
            Toast toast = new Toast(context);
            toast.setText("Kamera");
            toast.show();
            Intent fotoMachenIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri camUri = camUtil.getUri();
            imagePath = camUri.toString();
            fotoMachenIntent.putExtra(MediaStore.EXTRA_OUTPUT, camUri);
            landingPage.startActivityForResult(fotoMachenIntent,1);
            dialog.dismiss();
        });
        dialog.show();
    }

    public CircleImageView getPicture() {
        return picture;
    }

    public void setPicture(CircleImageView picture) {
        this.picture = picture;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
