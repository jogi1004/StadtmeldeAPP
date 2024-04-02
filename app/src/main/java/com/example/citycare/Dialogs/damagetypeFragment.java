package com.example.citycare.Dialogs;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.citycare.R;

public class damagetypeFragment extends Fragment implements View.OnClickListener {

    View rootView;
    ConstraintLayout streetDamage;
    ConstraintLayout vandalism;
    ConstraintLayout pollution;
    ConstraintLayout placeholder1;
    ConstraintLayout placeholder2;
    ConstraintLayout placeholder3;
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

        return rootView;
    }


    @Override
    public void onClick(View view) {
        if (view.equals(streetDamage)) {
            DetailedDamagetypeDialog ddd = new DetailedDamagetypeDialog(rootView.getContext());
            ddd.show();
        } else if (view.equals(vandalism)) {

        } else if (view.equals(pollution)) {

        } else if (view.equals(placeholder1)) {

        } else if (view.equals(placeholder2)) {

        } else if (view.equals(placeholder3)) {

        }
    }
}