package com.example.citycare;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.citycare.Dialogs.PoiInformationDialog;
import com.example.citycare.Dialogs.ProfilDialog;
import com.example.citycare.Dialogs.SearchDialog;
import com.example.citycare.Dialogs.fragment_damagetype;
import com.example.citycare.Dialogs.ReportDialogPage;
import com.example.citycare.Dialogs.SettingDialog;
import com.example.citycare.FAB.MyFloatingActionButtons;
import com.example.citycare.model.MainCategoryModel;
import com.example.citycare.model.ReportModel;
import com.example.citycare.util.APIHelper;
import com.example.citycare.util.CamUtil;
import com.example.citycare.util.AllReportsCallback;
import com.example.citycare.util.CategoryListCallback;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LandingPage extends AppCompatActivity implements MapListener, View.OnClickListener {

    private static MapView mMap;
    Marker poiMarker;
    IMapController controller;
    MyLocationNewOverlay mMyLocationOverlay;
    public FrameLayout dimm;
    public PoiInformationDialog poiInformationDialog;
    SearchDialog searchDialog;
    private static APIHelper apiHelper;
    private static List<MainCategoryModel> list = new ArrayList<>();
    private List<MainCategoryModel> fullList = new ArrayList<>();
    boolean alreadyCalled = false, isMember = false;
    private ArrayList<ReportModel> allReports = new ArrayList<>();
    private String cityName, tmp;
    ConstraintLayout compass;
    private CamUtil camUtil;
    private ProfilDialog profileDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing_page);
        dimm = findViewById(R.id.dimm);
        apiHelper = APIHelper.getInstance(this);
        camUtil=new CamUtil(this);
        Log.d("token", apiHelper.getToken() + "");

        compass = findViewById(R.id.compass);
        compass.setOnClickListener(this);

        initPermissions();
        poiInformationDialog = new PoiInformationDialog(this, this, getSupportFragmentManager());

        profileDialog = new ProfilDialog(this, this);
        ReportDialogPage allReportsDialog = new ReportDialogPage(this);
        SettingDialog settingDialog = new SettingDialog(this);
        searchDialog = new SearchDialog(this,this);
        new MyFloatingActionButtons(this, this, false, profileDialog, settingDialog, allReportsDialog, poiInformationDialog, searchDialog);

    }


    @SuppressLint("ObsoleteSdkInt")
    protected void initPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            loadMap();
            updateLocation();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        }
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 123);
        }
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, 124);
        }
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 125);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadMap();
            updateLocation();
        }
    }

    public void loadMap() {
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
        mMyLocationOverlay.runOnFirstFix(() -> runOnUiThread(() -> {
            controller.setCenter(mMyLocationOverlay.getMyLocation());
            controller.animateTo(mMyLocationOverlay.getMyLocation());
        }));

        controller.setZoom(18.0);
        mMap.getOverlays().add(0, mapEventsOverlay);
        mMap.getOverlays().add(mMyLocationOverlay);
        mMap.invalidate();
        mMap.addMapListener(this);
    }



    @SuppressLint("SetTextI18n")
    public void updatePoiMarker(GeoPoint geoPoint) {

        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
            assert addresses != null;
            cityName = addresses.get(0).getLocality();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        controller.animateTo(geoPoint);

        if(fullList.isEmpty()) {
            fillListWithData(cityName);
        }else if(!tmp.equals(cityName)){
            fullList.clear();
            fillListWithData(cityName);
        }

        poiMarker = new Marker(mMap);
        poiMarker.setPosition(geoPoint);
        poiMarker.setIcon(ContextCompat.getDrawable(this, R.drawable.png_poi_dark));

        mMap.getOverlays().add(poiMarker);


        poiInformationDialog.setOnDismissListener(dialog -> mMap.getOverlays().remove(poiMarker));

        try {
            Geocoder geo = new Geocoder(LandingPage.this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
            if (addresses.isEmpty()) {
                Toast toast = new Toast(this);
                toast.setText("Waiting for Location");
                toast.show();
            } else {
                poiInformationDialog.show();
                poiInformationDialog.fill(addresses);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        tmp = cityName;
    }

    private void fillListWithData(String cityName) {
        apiHelper.getMainCategorys(cityName, new CategoryListCallback() {

            @Override
            public void onSuccess(List<MainCategoryModel> categoryModels) {
                list = categoryModels;
                isMember = true;
                if (fragment_damagetype.adapter != null && !categoryModels.isEmpty()) {
                    fragment_damagetype.adapter.setData(list);
                }
                apiHelper.putSubCategories(new CategoryListCallback() {
                    @Override
                    public void onSuccess(List<MainCategoryModel> categoryModels) {
                        fullList = categoryModels;
                    }
                    @Override
                    public void onError(String errorMessage) {
                        Log.e("errorGetSubCategorys", errorMessage);
                    }
                }, list);
            }
            @Override
            public void onError(String errorMessage) {
                isMember = false;
                Log.e("errorGetMainCategorys", errorMessage);
            }
        });
    }

    @SuppressLint("MissingPermission")
    protected void updateLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this::onLocationReceived);
    }

    protected void onLocationReceived(Location location) {
        if(!alreadyCalled) {
            loadListfromDB(location);
            alreadyCalled = true;
        }
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

    public static List<MainCategoryModel> getMainCategoryList() {
        return list;
    }

    public static ArrayList<ReportModel> getAllReportsList() {
        return apiHelper.getAllReportsAsList();
    }

    public void loadExistingMarkers() {
        for (ReportModel m : allReports) {
            setMarker(m, this);
        }
    }

    public static void setMarker(ReportModel m, Context context){
        Marker poi = new Marker(mMap);
        GeoPoint geoP = new GeoPoint(m.getLatitude(), m.getLongitude());
        poi.setPosition(geoP);
        poi.setIcon(ContextCompat.getDrawable(context, R.drawable.png_poi));
        mMap.getOverlays().add(poi);
    }

    protected void loadListfromDB(Location location) {
        Geocoder geocoder = new Geocoder(this);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            assert addresses != null;
            String cityName = addresses.get(0).getLocality();
                apiHelper.getAllReports(cityName, new AllReportsCallback(){
                    @Override
                    public void onSuccess() {
                        allReports = apiHelper.getAllReportsAsList();
                        alreadyCalled = true;
                        loadExistingMarkers();
                    }
                    @Override
                    public void onError(String errorMessage) {
                        Log.e("Error in onLocationReceived: ", errorMessage);
                    }
                });
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClick(View view) {
        mMyLocationOverlay.runOnFirstFix(() -> runOnUiThread(() -> {
            controller.animateTo(mMyLocationOverlay.getMyLocation());
        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2 && data != null){
            Uri selectedImageUri = data.getData();
            Bitmap bitmap = camUtil.getBitmapFromUri(selectedImageUri);

            // Speichern Sie das Bild im internen Speicher
            profileDialog.saveImageToInternalStorage(bitmap);
            profileDialog.getPicture().setImageBitmap(bitmap);
            apiHelper.putProfilePicture(bitmap);


        } else if (requestCode == 1) {
            Bitmap bitmap = camUtil.getBitmap(profileDialog.getImageFile());
            profileDialog.getPicture().setImageBitmap(bitmap);
            apiHelper.putProfilePicture(bitmap);
        }
    }
}