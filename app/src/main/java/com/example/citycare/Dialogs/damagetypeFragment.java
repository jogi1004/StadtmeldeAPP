package com.example.citycare.Dialogs;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.citycare.R;

public class damagetypeFragment extends Fragment implements View.OnClickListener {

    View rootView;
    ConstraintLayout streetDamage;
    ConstraintLayout vandalism;
    ConstraintLayout pollution;
    ConstraintLayout placeholder1;
    ConstraintLayout placeholder2;
    ConstraintLayout placeholder3;
    DetailedDamagetypeDialog ddd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_damagetype, container, false);

        streetDamage = rootView.findViewById(R.id.strassenschaeden);
        streetDamage.setOnClickListener(this);

        vandalism = rootView.findViewById(R.id.vandalismus);
        vandalism.setOnClickListener(this);

        pollution = rootView.findViewById(R.id.verschmutzung);
        pollution.setOnClickListener(this);

        placeholder1 = rootView.findViewById(R.id.platzhalter1);
        placeholder1.setOnClickListener(this);

        placeholder2 = rootView.findViewById(R.id.platzhalter2);
        placeholder2.setOnClickListener(this);

        placeholder3 = rootView.findViewById(R.id.platzhalter3);
        placeholder3.setOnClickListener(this);

        ddd = new DetailedDamagetypeDialog(rootView);

        return rootView;
    }


    @Override
    public void onClick(View view) {

        if (view.equals(streetDamage)) {
            streetDamage.setBackground(ContextCompat.getDrawable(rootView.getContext(), R.drawable.bg_ddd_border));
            setView(streetDamage);
        }
        else if (view.equals(vandalism)) {
            vandalism.setBackground(ContextCompat.getDrawable(rootView.getContext(), R.drawable.bg_ddd_border));
            setView(vandalism);
        }
        else if (view.equals(pollution)) {
            pollution.setBackground(ContextCompat.getDrawable(rootView.getContext(), R.drawable.bg_ddd_border));
            setView(pollution);
        }
        else if (view.equals(placeholder1)) {
            placeholder1.setBackground(ContextCompat.getDrawable(rootView.getContext(), R.drawable.bg_ddd_border));
            setView(placeholder1);
        }
        else if (view.equals(placeholder2)) {
            placeholder2.setBackground(ContextCompat.getDrawable(rootView.getContext(), R.drawable.bg_ddd_border));
            setView(placeholder2);
        }
        else if (view.equals(placeholder3)) {
            placeholder3.setBackground(ContextCompat.getDrawable(rootView.getContext(), R.drawable.bg_ddd_border));
            setView(placeholder3);
        }

        ddd.show();
    }

    protected void setView(ConstraintLayout btn){

        int bx = btn.getLeft();
        int by = btn.getTop();

        Window window = ddd.getWindow();

        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.x = bx;
        wlp.y = (by - btn.getHeight() * 2);

        ddd.setWindow(wlp);
    }

}