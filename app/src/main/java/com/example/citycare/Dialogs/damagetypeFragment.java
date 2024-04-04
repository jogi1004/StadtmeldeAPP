package com.example.citycare.Dialogs;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.citycare.R;
import com.example.citycare.model.DamagetypeModel;
import com.example.citycare.util.RecyclerViewAdapter_Damagetype;

import java.util.ArrayList;

public class damagetypeFragment extends Fragment implements View.OnClickListener {

    View rootView;
    DetailedDamagetypeDialog ddd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_damagetype, container, false);

        ArrayList<DamagetypeModel> list = new ArrayList<DamagetypeModel>();
        list.add(new DamagetypeModel("Straßenschäden", R.drawable.png_placeholder));
        list.add(new DamagetypeModel("Vandalismus", R.drawable.png_placeholder));
        list.add(new DamagetypeModel("Tobias", R.drawable.png_placeholder));
        list.add(new DamagetypeModel("Tobias", R.drawable.png_placeholder));
        list.add(new DamagetypeModel("Tobias", R.drawable.png_placeholder));
        list.add(new DamagetypeModel("Straßenschäden", R.drawable.png_placeholder));
        list.add(new DamagetypeModel("Vandalismus", R.drawable.png_placeholder));
        list.add(new DamagetypeModel("Tobias", R.drawable.png_placeholder));
        list.add(new DamagetypeModel("Tobias", R.drawable.png_placeholder));
        list.add(new DamagetypeModel("Tobias", R.drawable.png_placeholder));

        RecyclerView recyclerView = rootView.findViewById(R.id.damageTypeRecyclerview);

        GridLayoutManager manager = new GridLayoutManager(rootView.getContext(), 2);
        Log.d("manageri", "height: " + recyclerView.getHeight());

        recyclerView.setLayoutManager(manager);

        RecyclerViewAdapter_Damagetype adapter = new RecyclerViewAdapter_Damagetype(rootView.getContext(), list);

        recyclerView.setAdapter(adapter);

        ddd = new DetailedDamagetypeDialog(rootView, getParentFragmentManager());

        return rootView;
    }


    @Override
    public void onClick(View view) {

//        streetDamage.setBackground(ContextCompat.getDrawable(rootView.getContext(), R.drawable.bg_ddd_border));
//        setView(streetDamage);

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