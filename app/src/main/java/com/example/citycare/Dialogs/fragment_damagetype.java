package com.example.citycare.Dialogs;

import static com.example.citycare.LandingPage.getMainCategoryList;

import android.app.Activity;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.citycare.LandingPage;
import com.example.citycare.R;
import com.example.citycare.model.IconModel;
import com.example.citycare.model.ReportModel;
import com.example.citycare.model.SubCategoryModel;
import com.example.citycare.util.APIHelper;
import com.example.citycare.util.OnItemClickListener;
import com.example.citycare.adapter.RecyclerViewAdapter_Categories;

import java.util.ArrayList;
import java.util.List;

public class fragment_damagetype extends Fragment implements OnItemClickListener {

    private Activity landingPage;
    private View rootView;
    private DetailedDamagetypeDialog ddd;
    private RecyclerView recyclerView;
    private ReportModel report;
    ImageView gifImageView;

    public  static RecyclerViewAdapter_Categories adapter;

    public fragment_damagetype(ReportModel report) {
        this.report = report;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_damagetype, container, false);

        adapter = new RecyclerViewAdapter_Categories(rootView.getContext(), new ArrayList<>());
        if (!getMainCategoryList().isEmpty()){
            adapter.setData(getMainCategoryList());
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
        List<SubCategoryModel> test = getMainCategoryList().get(position).getSubCategorys();
        if(test != null) {

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

            String category = getMainCategoryList().get(position).getTitle();
            report.setMainCategory(category);
            report.setIcon(new IconModel(getMainCategoryList().get(position).getIcon().getId(), getMainCategoryList().get(position).getIcon().getIcon()));

            ddd.prepList(position, report, gifImageView);

            FragmentDialog fragmentDialog = (FragmentDialog) getParentFragmentManager().findFragmentByTag("FragmentDialog");
            if (fragmentDialog != null) {
                fragmentDialog.dismiss();
            }

            ddd.show();
            ddd.setOnDismissListener(v -> field.setBackground(ContextCompat.getDrawable(rootView.getContext(), R.drawable.bg_report)));
        }else{
            Toast.makeText(this.getContext(), "Versuche es nochmal", Toast.LENGTH_SHORT).show();
        }
    }

}