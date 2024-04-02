package com.example.citycare.Dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.citycare.R;

public class DetailedDamagetypeDialog extends Dialog implements DialogInterface.OnDismissListener {

    Dialog DetailedDamagetypeDialog;
    Context context;
    View rootView;

    public DetailedDamagetypeDialog(View rootView) {
        super(rootView.getContext());
        this.context = rootView.getContext();
        this.rootView = rootView;
        setOnDismissListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DetailedDamagetypeDialog = new Dialog(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.detailed_damagetype_dialog);

    }

    protected void setWindow(WindowManager.LayoutParams params){
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(params);
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        rootView.findViewById(R.id.strassenschaeden).setBackground(ContextCompat.getDrawable(context, R.drawable.bg_report));
        rootView.findViewById(R.id.vandalismus).setBackground(ContextCompat.getDrawable(context, R.drawable.bg_report));
        rootView.findViewById(R.id.verschmutzung).setBackground(ContextCompat.getDrawable(context, R.drawable.bg_report));
        rootView.findViewById(R.id.platzhalter1).setBackground(ContextCompat.getDrawable(context, R.drawable.bg_report));
        rootView.findViewById(R.id.platzhalter2).setBackground(ContextCompat.getDrawable(context, R.drawable.bg_report));
        rootView.findViewById(R.id.platzhalter3).setBackground(ContextCompat.getDrawable(context, R.drawable.bg_report));
    }
}
