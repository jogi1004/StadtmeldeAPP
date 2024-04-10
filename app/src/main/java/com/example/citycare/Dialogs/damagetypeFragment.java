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
import android.widget.Toast;

import com.example.citycare.LandingPage;
import com.example.citycare.R;
import com.example.citycare.util.APIHelper;
import com.example.citycare.util.OnItemClickListener;
import com.example.citycare.adapter.RecyclerViewAdapter_Categories;

import java.util.ArrayList;

public class damagetypeFragment extends Fragment implements OnItemClickListener {

    private View rootView;
    private DetailedDamagetypeDialog ddd;
    private RecyclerView recyclerView;
    private APIHelper apiHelper;

    public  static RecyclerViewAdapter_Categories adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_damagetype, container, false);

        adapter = new RecyclerViewAdapter_Categories(rootView.getContext(), new ArrayList<>());
        if (!LandingPage.getList().isEmpty()){
            adapter.setData(LandingPage.getList());
        }

        recyclerView = rootView.findViewById(R.id.damageTypeRecyclerview);

        GridLayoutManager manager = new GridLayoutManager(rootView.getContext(), 2);
        recyclerView.setLayoutManager(manager);


        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        ddd = new DetailedDamagetypeDialog(rootView, getParentFragmentManager());


        return rootView;
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(rootView.getContext(), "Item " + position, Toast.LENGTH_SHORT).show();

        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        RecyclerViewAdapter_Categories.MyViewHolder yourViewHolder = (RecyclerViewAdapter_Categories.MyViewHolder) viewHolder;
        ConstraintLayout field = yourViewHolder.field;
        field.setBackground(ContextCompat.getDrawable(rootView.getContext(), R.drawable.bg_ddd_border));

        View w = (View) field.getParent();

        if (w != null) {
            int[] location = new int[2];
            w.getLocationOnScreen(location);
            int y = location[1];

            Window window = ddd.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.x = 0;
            wlp.y = (int) (y - w.getHeight() * 1.5);

            ddd.setWindow(wlp);
        }
        ddd.prepList(position);

        ddd.show();
        ddd.setOnDismissListener(v-> field.setBackground(ContextCompat.getDrawable(rootView.getContext(), R.drawable.bg_report)));
    }

}