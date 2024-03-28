package com.example.citycare;

import  android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;



public class LandingPage extends AppCompatActivity implements MapListener {

    MapView mMap;
    Marker poiMarker;
    IMapController controller;
    MyLocationNewOverlay mMyLocationOverlay;

    private Dialog profileDialog;
    private Dialog poiInformationDialog;
    private FloatingActionButton profilFAB, addFAB, allReportsFAB, settingsFAB;
    private Boolean areFabsVisible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.landing_page);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        initPermissions();
        initFABMenu();
        initProfilDialog();
        initPoiInformationsDialog();
    }

    @SuppressLint("ObsoleteSdkInt")
    protected void initPermissions(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            loadMap();
            updateLocation();
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            loadMap();
            updateLocation();
        }
    }

    protected void loadMap(){
        Configuration.getInstance().load(
                getApplicationContext(),
                getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        );

        mMap = findViewById(R.id.osmmap);
        mMap.setTileSource(TileSourceFactory.MAPNIK);
        mMap.setMultiTouchControls(true);
        mMap.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
                updatePoiMarker(new GeoPoint(geoPoint.getLatitude(), geoPoint.getLongitude()));
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint geoPoint) {
                return false;
            }
        });


        mMyLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mMap);
        controller = mMap.getController();

        mMyLocationOverlay.enableMyLocation();
        mMyLocationOverlay.enableFollowLocation();
        mMyLocationOverlay.setDrawAccuracyEnabled(true);
        mMyLocationOverlay.runOnFirstFix(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        controller.setCenter(mMyLocationOverlay.getMyLocation());
                        controller.animateTo(mMyLocationOverlay.getMyLocation());
                    }
                });
            }
        });
        controller.setZoom(18.0);
        mMap.getOverlays().add(0, mapEventsOverlay); // Das Overlay an den Anfang der Liste hinzufügen
        mMap.getOverlays().add(mMyLocationOverlay);
        mMap.invalidate();
        mMap.addMapListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void updatePoiMarker(GeoPoint geoPoint) {

        poiMarker = new Marker(mMap);
        poiMarker.setPosition(geoPoint);
        //poiMarker.setIcon(ContextCompat.getDrawable(this, R.mipmap.poiklein));
        mMap.getOverlays().add(poiMarker);

        poiInformationDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mMap.getOverlays().remove(poiMarker);
            }
        });

        TextView adress = poiInformationDialog.findViewById(R.id.adress);
        TextView adressInfos = poiInformationDialog.findViewById(R.id.adressInfos);
        TextView koords = poiInformationDialog.findViewById(R.id.koords);

        try{
            Geocoder geo = new Geocoder(LandingPage.this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
            if (addresses.isEmpty()) {
                Toast toast = new Toast(this);
                toast.setText("Waiting for Location");
                toast.show();
            }
            else {
                String address[] = addresses.get(0).getAddressLine(0).split(", ");
                adress.setText(address[0]);
                if(addresses.get(0).getSubLocality() == null){
                    adressInfos.setText(address[0] + ", " + addresses.get(0).getLocality());
                }else {
                    adressInfos.setText(address[0] + ", " + addresses.get(0).getSubLocality());
                }
                koords.setText(addresses.get(0).getLatitude() + ", " + addresses.get(0).getLongitude());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        poiInformationDialog.show();

    }

    @SuppressLint("MissingPermission")
    protected void updateLocation(){
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this::onLocationReceived);
    }

    protected void onLocationReceived(Location location){

    }

    @Override
    public boolean onScroll(ScrollEvent event) {
        return true;
    }

    @Override
    public boolean onZoom(ZoomEvent event) {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);

    }


    private void initProfilDialog(){
        profileDialog = new Dialog(this);
        profileDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        profileDialog.setContentView(R.layout.profile_dialog);
        profileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        profileDialog.setOnDismissListener(dialog -> {
            profilFAB.setVisibility(View.GONE);
            areFabsVisible=false;
        });

        Window window = profileDialog.getWindow();
        window.setGravity(Gravity.TOP);
        window.setDimAmount(0.0f);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void initPoiInformationsDialog(){
        poiInformationDialog = new Dialog(this);
        poiInformationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        poiInformationDialog.setContentView(R.layout.poi_informations);
        poiInformationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        poiInformationDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        Window window = poiInformationDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private void initFABMenu(){
        profilFAB = findViewById(R.id.profil);
        addFAB = findViewById(R.id.addReports);
        allReportsFAB = findViewById(R.id.allReports);
        settingsFAB = findViewById(R.id.setting);
        FloatingActionButton menuFAB = findViewById(R.id.menu);


        //Hide Buttons
        addFAB.setVisibility(View.GONE);
        profilFAB.setVisibility(View.GONE);
        allReportsFAB.setVisibility(View.GONE);
        settingsFAB.setVisibility(View.GONE);
        areFabsVisible = false;

        menuFAB.setOnClickListener(v->toggleFABMenu());
        profilFAB.setOnClickListener(v -> {
            profileDialog.show();
            //Hide all other FAB Buttons
            addFAB.setVisibility(View.GONE);
            allReportsFAB.setVisibility(View.GONE);
            settingsFAB.setVisibility(View.GONE);

        });
    }
    private void toggleFABMenu(){
        if(!areFabsVisible){
            //show
            profilFAB.show();
            addFAB.show();
            allReportsFAB.show();
            settingsFAB.show();

            areFabsVisible = true;
        }else{
            //hide
            profilFAB.hide();
            addFAB.hide();
            allReportsFAB.hide();
            settingsFAB.hide();

            areFabsVisible = false;
        }
    }
}