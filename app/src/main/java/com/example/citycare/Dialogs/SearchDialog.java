package com.example.citycare.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.citycare.LandingPage;
import com.example.citycare.R;
import com.example.citycare.util.APIHelper;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.List;

public class SearchDialog extends Dialog implements View.OnClickListener {

    Context context;
    FrameLayout dimm;
    private Geocoder geocoder;
    private EditText address;
    private ConstraintLayout searchBtn;
    LandingPage landingPage;
    APIHelper apiHelper;
    private PoiInformationDialog poiInformationDialog;

    public SearchDialog(@NonNull Context context, LandingPage landingPage, PoiInformationDialog poiInformationDialog) {
        super(context);
        this.context = context;
        this.dimm = findViewById(R.id.dimm);
        this.landingPage = landingPage;
        this.poiInformationDialog = poiInformationDialog;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_search);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        apiHelper = APIHelper.getInstance(context);

        address = findViewById(R.id.searchBar);
        geocoder = new Geocoder(this.context);

        searchBtn = findViewById(R.id.search_button);
        searchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String query = address.getText().toString();
        if (!query.isEmpty()) {
            performSearch(query);
        }
    }

    private void performSearch(String query) {
        //TODO Maybe noch auswahlmöglichkeiten geben falls mehrere Mögliche Results
        try {
            List<Address> addresses = geocoder.getFromLocationName(query, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                String result = "Address: " + address.getAddressLine(0) +
                        "\nLatitude: " + address.getLatitude() +
                        "\nLongitude: " + address.getLongitude();
                Log.d("Address", result);

                landingPage.updatePoiMarker(new GeoPoint(address.getLatitude(), address.getLongitude()));
                apiHelper.getIsLocationMember(new GeoPoint(address.getLatitude(), address.getLongitude()), landingPage, poiInformationDialog);

                dismiss();
            } else {
                Log.d("Search","No results found for query: " + query);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Search","Error: " + e.getMessage());
        }
    }

}
