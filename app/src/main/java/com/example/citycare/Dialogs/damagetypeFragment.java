package com.example.citycare.Dialogs;

import static androidx.core.view.ViewCompat.setBackground;

import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.example.citycare.R;
import com.example.citycare.model.DamagetypeModel;
import com.example.citycare.util.OnItemClickListener;
import com.example.citycare.util.RecyclerViewAdapter_Damagetype;

import java.util.ArrayList;

public class damagetypeFragment extends Fragment implements OnItemClickListener {

    private View rootView;
    private DetailedDamagetypeDialog ddd;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_damagetype, container, false);

        ArrayList<DamagetypeModel> list = setUpData();

        recyclerView = rootView.findViewById(R.id.damageTypeRecyclerview);


        GridLayoutManager manager = new GridLayoutManager(rootView.getContext(), 2);
        recyclerView.setLayoutManager(manager);

        RecyclerViewAdapter_Damagetype adapter = new RecyclerViewAdapter_Damagetype(rootView.getContext(), list);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);


        ddd = new DetailedDamagetypeDialog(rootView, getParentFragmentManager());

        return rootView;
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(rootView.getContext(), "Item " + position, Toast.LENGTH_SHORT).show();

        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        RecyclerViewAdapter_Damagetype.MyViewHolder yourViewHolder = (RecyclerViewAdapter_Damagetype.MyViewHolder) viewHolder;
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
            ddd.show();

            ddd.setOnDismissListener(v->{
                field.setBackground(ContextCompat.getDrawable(rootView.getContext(), R.drawable.bg_report));
            });
        }
    }

    @NonNull
    private static ArrayList<DamagetypeModel> setUpData() {
        ArrayList<DamagetypeModel> list = new ArrayList<>();
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
        return list;
    }

}