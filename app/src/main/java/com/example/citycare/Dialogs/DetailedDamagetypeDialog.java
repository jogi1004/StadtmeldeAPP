package com.example.citycare.Dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.citycare.LandingPage;
import com.example.citycare.R;
import com.example.citycare.adapter.RecyclerViewAdapter_Categories;
import com.example.citycare.adapter.RecyclerViewAdapter_SubCategories;
import com.example.citycare.model.MainCategoryModel;
import com.example.citycare.model.SubCategoryModel;
import com.example.citycare.util.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class DetailedDamagetypeDialog extends Dialog implements View.OnClickListener, OnItemClickListener {

    Dialog DetailedDamagetypeDialog;
    private final Context context;
    private final View rootView;
    private EditText other;
    private final FragmentManager fragmentManager;
    private damagetitleFragment damageTitleF;
    private FragmentTransaction transaction;
    private TextView title;
    RecyclerViewAdapter_SubCategories adapter;
    private RecyclerView recyclerView;
    private List<MainCategoryModel> listAll;
    private List<SubCategoryModel> listSub = new ArrayList<>();

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
        recyclerView = findViewById(R.id.subCategoryView);

        adapter = new RecyclerViewAdapter_SubCategories(rootView.getContext(), listSub);
        adapter.setOnItemClickListener(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


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


//        if(view == tv1){
//            title.setText(tv1.getText());
//        } else if (view == tv2) {
//            title.setText(tv2.getText());
//        } else{
//            assert view == tv3;
//            title.setText(tv3.getText());
//        }


    }

    @SuppressLint("NotifyDataSetChanged")
    public void prepList(int position) {
        if(!listSub.isEmpty()){
            listSub.clear();
        }

        listAll = LandingPage.getMainCategoryList();
        listSub.addAll(listAll.get(position).getSubCategorys());
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(int position) {
        dismiss();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.flFragment, damageTitleF);
        transaction.commitNow();

        title = damageTitleF.getView().findViewById(R.id.title);

        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        RecyclerViewAdapter_SubCategories.MyViewHolder myViewHolder = (RecyclerViewAdapter_SubCategories.MyViewHolder) viewHolder;
        TextView subCategoryTitle = myViewHolder.title;

        title.setText(subCategoryTitle.getText());
    }
}
