package com.example.citycare.Dialogs;

import static com.example.citycare.R.layout.dialog_report;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.citycare.R;
import com.example.citycare.model.ReportModel;

public class FragmentDialog extends DialogFragment {

    private FrameLayout dimm;
    private ReportModel report;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Log.d("BackPressed", "drin");
                // Deine Zurück-Taste Behandlung hier
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                } else {
                    // Wenn kein Eintrag im Fragment-Manager-Backstack vorhanden ist,
                    // kannst du den Dialog schließen
                    dismiss();
                }
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(dialog_report, container, false);
        getDialog().getWindow().setDimAmount(0.0f);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Fragment damageTypeF = new fragment_damagetype(report);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.flFragment, damageTypeF);
        transaction.commit();

    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        if (window != null) {

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int dialogheight = (int) (displayMetrics.heightPixels * 0.85);

            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, dialogheight);
            window.setGravity(Gravity.TOP);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        dimm.setVisibility(View.GONE);
    }

    public void showFragmentDialog(FragmentManager fragmentManager, FrameLayout dimm, double lat, double lon, String locationName) {
        if (fragmentManager != null) {
            show(fragmentManager, "FragmentDialog");
            report = new ReportModel(null, null, null, null, null, lon, lat, null, locationName);
            this.dimm = dimm;
        }
    }


}
