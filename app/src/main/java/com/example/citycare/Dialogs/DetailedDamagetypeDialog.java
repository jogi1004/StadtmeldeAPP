package com.example.citycare.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.citycare.R;

public class DetailedDamagetypeDialog extends Dialog implements View.OnClickListener {

    Dialog DetailedDamagetypeDialog;
    private final Context context;
    private final View rootView;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private EditText other;
    private final FragmentManager fragmentManager;
    private damagetitleFragment damageTitleF;
    private FragmentTransaction transaction;
    private TextView title;

    public DetailedDamagetypeDialog(View rootView, FragmentManager fragmentManager) {
        super(rootView.getContext());
        this.context = rootView.getContext();
        this.rootView = rootView;
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DetailedDamagetypeDialog = new Dialog(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_detailed_damagetype);

        damageTitleF = new damagetitleFragment();

        tv1 = findViewById(R.id.textView1);
        tv1.setOnClickListener(this);
        tv2 = findViewById(R.id.textView2);
        tv2.setOnClickListener(this);
        tv3 = findViewById(R.id.textView3);
        tv3.setOnClickListener(this);
        other = findViewById(R.id.other);
        other.setOnEditorActionListener((textView, i, keyEvent) -> {
            dismiss();

            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.flFragment, damageTitleF);
            transaction.commitNow();
            title = damageTitleF.getView().findViewById(R.id.title);
            title.setText(other.getText());
            return true;
        });
    }

    protected void setWindow(WindowManager.LayoutParams params){
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(params);
    }

    @Override
    public void onClick(View view) {

        dismiss();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.flFragment, damageTitleF);
        transaction.commitNow();

        title = damageTitleF.getView().findViewById(R.id.title);


        if(view == tv1){
            title.setText(tv1.getText());
        } else if (view == tv2) {
            title.setText(tv2.getText());
        } else{
            assert view == tv3;
            title.setText(tv3.getText());
        }


    }
}
