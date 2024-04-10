package com.example.citycare.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.citycare.R;

public class fragment_damagetitle extends Fragment implements View.OnClickListener {

    public TextView title;
    private FragmentTransaction transaction;
    private fragment_cam camF;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_title, container, false);

        title = rootView.findViewById(R.id.title);
        ImageButton nextFragment = rootView.findViewById(R.id.nextFragment);
        nextFragment.setOnClickListener(this);

        camF = new fragment_cam();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        final FragmentManager fragmentManager = getParentFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.flFragment, camF);
        transaction.commitNow();
    }
}