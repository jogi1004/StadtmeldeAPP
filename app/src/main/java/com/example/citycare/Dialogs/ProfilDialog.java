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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.citycare.R;
import com.example.citycare.WelcomePage;
import com.example.citycare.adapter.RecyclerViewAdapter_AllReports;
import com.example.citycare.model.ReportModel;
import com.example.citycare.util.APIHelper;
import com.example.citycare.util.AllReportsCallback;
import com.example.citycare.util.CamUtil;
import com.example.citycare.util.RecyclerViewInterface;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilDialog extends Dialog implements RecyclerViewInterface {

    private Context context;
    private FrameLayout dimm;
    private APIHelper apiHelper;
    private CircleImageView picture;
    public Activity landingPage;
    private CamUtil camUtil;
    private TextView username;
    private ImageView gifImageView;
    private List<ReportModel> userReports = new ArrayList<>();
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

        gifImageView = findViewById(R.id.gifProfil);
        Glide.with(context).asGif().load(R.drawable.gif_punkte_laden).into(gifImageView);


        RecyclerView recyclerView = findViewById(R.id.personalReportsView);
        RecyclerViewAdapter_AllReports recyclerAdapter = new RecyclerViewAdapter_AllReports(context, new ArrayList<>());
        recyclerAdapter.setRecyclerViewInterface(this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        apiHelper.getUserReports(new AllReportsCallback() {
            @Override
            public void onSuccess(List<ReportModel> reports) {
                recyclerAdapter.updateList(reports);
                Log.d("updateallReportsProfil", String.valueOf(reports.size()));
                gifImageView.setVisibility(View.GONE);
                Log.d("reports", "da");
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("getUserReportsError", errorMessage);
            }
        });


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
        if (apiHelper.getCurrentUser().getProfilePicture()!=null){
            picture.setImageBitmap(apiHelper.getCurrentUser().getProfilePicture());
            picture.setRotation(camUtil.showImage());
        }else {
            picture.setImageResource(R.drawable.png_dummy);
        }
        picture.setOnClickListener(v -> camUtil.initDialog(2,1));
    }



    public CircleImageView getPicture() {
        return picture;
    }
    public File getImageFile() {
        return camUtil.getImageFile();
    }

    @Override
    public void onItemClick(int position) {

    }
}
