package com.example.citycare.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.citycare.LandingPage;
import com.example.citycare.R;
import com.example.citycare.util.APIHelper;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchDialog extends Dialog implements View.OnClickListener {

    Context context;
    FrameLayout dimm;
    private Geocoder geocoder;
    private EditText address;
    private ConstraintLayout searchBtn;
    LandingPage landingPage;
    APIHelper apiHelper;
    private PoiInformationDialog poiInformationDialog;
    private List<Address> lastAdresses = new ArrayList<>();
    TextView address1, address2, address3;
    private static final String PREF_LASTADRESSES = "lastAdresses";
    private ConstraintLayout layout1, layout2, layout3;

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

        address1 = findViewById(R.id.adress1);
        address2 = findViewById(R.id.adress2);
        address3 = findViewById(R.id.adress3);
        address1.setText("");
        address2.setText("");
        address3.setText("");

        layout1 = findViewById(R.id.layout1);
        layout1.setOnClickListener(this);
        layout2 = findViewById(R.id.layout2);
        layout2.setOnClickListener(this);
        layout3 = findViewById(R.id.layout3);
        layout3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        SharedPreferences SPLastAdresses = context.getSharedPreferences(PREF_LASTADRESSES, Context.MODE_PRIVATE);
        String inside = "";

        if(view == searchBtn) {
            String query = address.getText().toString();
            address.setText("");
            if (!query.isEmpty()) {
                performSearch(query);
            }
            return;
        }else if(view == layout1){
            inside = SPLastAdresses.getString("LastAddresses1", null);
            Log.d("lastAddresses", "1: " + inside);
        }else if(view == layout2){
            inside = SPLastAdresses.getString("LastAddresses2", null);
            Log.d("lastAddresses", "2:" + inside);
        }else{
            assert view == layout3;
            inside = SPLastAdresses.getString("LastAddresses3", null);
            Log.d("lastAddresses", "3: " + inside);
        }

        GeoPoint geoPoint = convertText(inside);
        Log.d("lastAddresses", "geopoint: " + geoPoint);
        landingPage.updatePoiMarker(geoPoint);
        apiHelper.getIsLocationMember(geoPoint, landingPage, poiInformationDialog);
        dismiss();

    }

    private GeoPoint convertText(String inside) {
        String[] teile = inside.split(",");
        return new GeoPoint(Double.parseDouble(teile[teile.length-2]), Double.parseDouble(teile[teile.length-1]));
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


                SharedPreferences SPLastAdresses = context.getSharedPreferences(PREF_LASTADRESSES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = SPLastAdresses.edit();

                Map<String, ?> allEntries = SPLastAdresses.getAll();

                if(allEntries.size() > 2){
                    String value1 = SPLastAdresses.getString("LastAddresses2", null);
                    editor.putString("LastAddresses1", value1);

                    String value2 = SPLastAdresses.getString("LastAddresses3", null);
                    editor.putString("LastAddresses2", value2);

                    editor.putString("LastAddresses3", query + "," + addresses.get(0).getLatitude() + "," +  addresses.get(0).getLongitude());

                    editor.apply();
                }else{
                    Log.d("lastAddresses", "query: " + query);
                    editor.putString("LastAddresses" + (allEntries.size()+ 1), query + "," + addresses.get(0).getLongitude() + "," +  addresses.get(0).getLatitude());

                    editor.apply();
                }
                dismiss();
            } else {
                Toast.makeText(context, "Keine Ergebnisse für die Suche: " + query, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Search","Error: " + e.getMessage());
        }
    }

}
