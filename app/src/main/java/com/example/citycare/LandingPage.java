package com.example.citycare;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.citycare.Dialogs.PoiDialog;
import com.example.citycare.Dialogs.PoiInformationDialog;
import com.example.citycare.Dialogs.ProfilDialog;
import com.example.citycare.Dialogs.SearchDialog;
import com.example.citycare.Dialogs.fragment_damagetype;
import com.example.citycare.Dialogs.ReportDialogPage;
import com.example.citycare.Dialogs.SettingDialog;
import com.example.citycare.FAB.MyFloatingActionButtons;
import com.example.citycare.adapter.RecyclerViewAdapter_AllReports;
import com.example.citycare.model.MainCategoryModel;
import com.example.citycare.model.ReportModel;
import com.example.citycare.util.APIHelper;
import com.example.citycare.util.CamUtil;
import com.example.citycare.util.AllReportsCallback;
import com.example.citycare.util.CategoryListCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/**
 *  LandingPage contains MapView with FAB for further Depth in the Application, acts like Homepage for User
 */
public class LandingPage extends AppCompatActivity implements MapListener, View.OnClickListener {

    private static MapView mMap;
    private Marker poiMarker;
    private IMapController controller;
    private MyLocationNewOverlay mMyLocationOverlay;
    public FrameLayout dimm;
    public ProfilDialog profileDialog;
    public PoiInformationDialog poiInformationDialog;
    private SearchDialog searchDialog;
    private static APIHelper apiHelper;
    private static List<MainCategoryModel> mainCategoryList = new ArrayList<>();
    private List<MainCategoryModel> fullList = new ArrayList<>();
    boolean alreadyCalled = false, isMember = false;
    private static List<ReportModel> allReports = new ArrayList<>();
    private List<ReportModel> allReportsUpdated = new ArrayList<>();
    private String cityName, tmp;
    private ConstraintLayout compass;
    private static CamUtil camUtil;
    private static ImageView reportImageView;
    private static final String PREF_NAME = "report_preferences";
    private static final String KEY_REPORT_LIST = "report_list";
    private static SharedPreferences sharedPreferences;
    private static Context context;
    private static MyFloatingActionButtons myFloatingActionButtons;

    private static RecyclerViewAdapter_AllReports adapterAllReports;

    /**
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *      is called when Application starts or after Environmental changes like Darkmode
     *      camUtil is used for using Camera, apiHelper assists database connections, compass is for User to center
     *      the location in the middle of screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        dimm = findViewById(R.id.dimm);
        apiHelper = APIHelper.getInstance(this);
        camUtil = new CamUtil(this, this
        );
        context = this;
        Log.d("token", apiHelper.getToken() + "");

        //um status und navbar komplett transparent zu machen
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);*/

        //

        /*setStatusBarTransparent();*/
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        compass = findViewById(R.id.compass);
        compass.setOnClickListener(this);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d("BackPressed", "drin");
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                }
                finish();
            }
        });

        // ask for permission to use camera and location
        initPermissions();
        // initialize dialogs for FAB
        poiInformationDialog = new PoiInformationDialog(this, this, getSupportFragmentManager());
        profileDialog = new ProfilDialog(this, this, camUtil);
        ReportDialogPage allReportsDialog = new ReportDialogPage(this, this, adapterAllReports);
        SettingDialog settingDialog = new SettingDialog(this);
        searchDialog = new SearchDialog(this, this, poiInformationDialog);
        myFloatingActionButtons = new MyFloatingActionButtons(this, this, false, profileDialog, settingDialog, allReportsDialog, poiInformationDialog, searchDialog);
    }

    public static MyFloatingActionButtons getMyFloatingActionButtons() {
        return myFloatingActionButtons;
    }

    private void setStatusBarTransparent() {
        Window window = getWindow();
        WindowInsetsController controller = getWindow().getInsetsController();
        if (window != null) {
            // Statusleiste transparent machen


            // Navigationsleistenfarbe beibehalten
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.black));


        }
    }

    /**
     * initPermissions asks User for permissions like using camera or location
     */
    protected void initPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            loadMap();
            updateLocation();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }

    /**
     * loads map after opening the app and sets controller to location of user and zoom of the map
     */
    public void loadMap() {
        Configuration.getInstance().load(
                getApplicationContext(),
                getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        );

        mMap = findViewById(R.id.osmmap);
        int navigationBarHeight = getResources().getDimensionPixelSize(R.dimen.navigation_bar_height);
        mMap.setPadding(0, 0, 0, navigationBarHeight);
        mMap.setTileSource(TileSourceFactory.MAPNIK);
        mMap.setMultiTouchControls(true);
        mMap.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        Geocoder geocoder = new Geocoder(this);

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
                updatePoiMarker(geoPoint);
                String location;

                try {
                    List<Address> addresses = geocoder.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
                    assert addresses != null;
                    location = addresses.get(0).getLocality();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (apiHelper.getMembers().contains(location)) {
                    poiInformationDialog.setGifVisibility(View.GONE);
                    poiInformationDialog.setButtonVisibility(View.VISIBLE);
                } else if (apiHelper.getNotMembers().contains(location)) {
                    poiInformationDialog.setGifVisibility(View.GONE);
                    poiInformationDialog.setButtonVisibility(View.INVISIBLE);
                    Toast.makeText(poiInformationDialog.getContext(), "Stadt ist kein Mitglied", Toast.LENGTH_LONG).show();
                } else {
                    apiHelper.getIsLocationMember(geoPoint, LandingPage.this, poiInformationDialog);
                }
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

    /**
     *
     * @param geoPoint is the method to set an marker on an point where the user wants to report something
     *                 geocoder assists to get the exact location from the touch event
     */
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

        if (fullList.isEmpty()) {
            fillListWithData(cityName);
        } else if (!tmp.equals(cityName)) {
            fullList.clear();
            fillListWithData(cityName);
        }

        poiMarker = new Marker(mMap);
        poiMarker.setPosition(geoPoint);
        poiMarker.setIcon(ContextCompat.getDrawable(this, R.drawable.png_poi_dark));

        mMap.getOverlays().add(poiMarker);


        poiInformationDialog.setOnDismissListener(dialog -> {
            mMap.getOverlays().remove(poiMarker);
            poiInformationDialog.setGifVisibility(View.VISIBLE);
            poiInformationDialog.setButtonVisibility(View.GONE);
        });

        try {
            Geocoder geo = new Geocoder(LandingPage.this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
            if (addresses.isEmpty()) {
                Toast toast = new Toast(this);
                toast.setText("Waiting for Location");
                toast.show();
            } else {
                poiInformationDialog.show();
                myFloatingActionButtons.hideFABS();

                ConstraintLayout createReportBtn = poiInformationDialog.findViewById(R.id.reportButton);

                if (createReportBtn.getVisibility() == View.INVISIBLE) {
                    createReportBtn.setClickable(true);
                    createReportBtn.setVisibility(View.VISIBLE);
                }
                poiInformationDialog.fill(addresses);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        tmp = cityName;
    }

    /**
     * after receiving location/cityname the app loads the categories from database
     * @param cityName from geocoder
     */
    private void fillListWithData(String cityName) {
        apiHelper.getMainCategorys(cityName, new CategoryListCallback() {

            @Override
            public void onSuccess(List<MainCategoryModel> categoryModels) {
                mainCategoryList = categoryModels;
                isMember = true;
                if (fragment_damagetype.adapter != null && !categoryModels.isEmpty()) {
                    fragment_damagetype.adapter.setData(mainCategoryList);
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
                }, mainCategoryList);
            }

            @Override
            public void onError(String errorMessage) {
                isMember = false;
                Log.e("errorGetMainCategorys", errorMessage);
            }
        });
    }

    /**
     * updates location from the user
     */
    @SuppressLint("MissingPermission")
    protected void updateLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this::onLocationReceived);
    }

    /**
     * calls method for loading reports at the given location
     * @param location of the user
     */
    protected void onLocationReceived(Location location) {
        if (!alreadyCalled) {
            alreadyCalled = true;
            Toast.makeText(this, "Aktualisiere Meldungen...", Toast.LENGTH_LONG).show();
            loadListfromDB(location);

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
        return mainCategoryList;
    }

    public static List<ReportModel> getAllReportsList() {
        return allReports;
    }

    /**
     * loads the reports from allReports List
     */
    public void loadExistingMarkers() {
        for (ReportModel m : allReports) {
            setMarker(m, this);
        }
    }

    /**
     * sets POI on Maps where a report was reported
     * @param m ReportModel for getting longitude and latitude to set POI
     * @param context
     */

    public static void setMarker(ReportModel m, Context context) {
        Marker poi = new Marker(mMap);
        GeoPoint geoP = new GeoPoint(m.getLatitude(), m.getLongitude());
        poi.setPosition(geoP);
        poi.setIcon(ContextCompat.getDrawable(context, R.drawable.png_poi));


        poi.setOnMarkerClickListener((marker, mapView) -> {
            PoiDialog poiDialog = new PoiDialog(context,m);
            poiDialog.show();
            myFloatingActionButtons.hideFABS();
            return true;
        });

        mMap.getOverlays().add(poi);
    }

    /**
     * loads reportList from database based on current loation, only at first start of the app
     * after initialy loading the list it gets saved in SharedPrefs and after every starting updated
     * @param location
     */
    protected void loadListfromDB(Location location) {
        Geocoder geocoder = new Geocoder(this);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            assert addresses != null;
            String cityName = addresses.get(0).getLocality();
            sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            if (sharedPreferences.contains(KEY_REPORT_LIST)) {
                Gson gson = new Gson();
                String json = sharedPreferences.getString(KEY_REPORT_LIST, null);
                Type type = new TypeToken<List<ReportModel>>() {
                }.getType();
                allReports = gson.fromJson(json, type);
                loadExistingMarkers();
                apiHelper.getAllReports(cityName, new AllReportsCallback() {

                    @Override
                    public void onSuccess(List<ReportModel> reports) {
                        allReportsUpdated = reports;
                        allReports.clear();
                        allReports = allReportsUpdated;
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(allReports);
                        editor.putString(KEY_REPORT_LIST, json);
                        editor.apply();
                        loadExistingMarkers();
                        Toast.makeText(LandingPage.this, "Meldungen aktualisiert!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.e("Error in checkingListsOfEquality: ", errorMessage);
                    }
                });

            } else {
                if(WelcomePage.loggedIn && apiHelper.getToken() != null)
                    {
                        apiHelper.getAllReports(cityName, new AllReportsCallback() {
                            @Override
                            public void onSuccess(List<ReportModel> reports) {
                                allReports = apiHelper.getAllReportsAsList();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(allReports);
                                editor.putString(KEY_REPORT_LIST, json);
                                editor.apply();
                                alreadyCalled = true;
                                loadExistingMarkers();
                                Toast.makeText(LandingPage.this, "Meldungen aktualisiert!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(String errorMessage) {
                                Log.e("Error in onLocationReceived: ", errorMessage);
                            }
                        });
                    apiHelper.getAllReports(cityName, new AllReportsCallback() {

                        @Override
                        public void onSuccess(List<ReportModel> reports) {
                            allReports = reports;
                            loadExistingMarkers();
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("Error in onLocationReceived: ", errorMessage);
                        }
                    });
                } else {
                    try{
                        Thread.sleep(1000);
                        loadListfromDB(location);
                    } catch(InterruptedException e){
                        Log.e("InterruptedException", String.valueOf(e));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClick(View view) {
        mMyLocationOverlay.runOnFirstFix(() -> runOnUiThread(() -> {
            controller.animateTo(mMyLocationOverlay.getMyLocation());
            controller.setZoom(18.0);
        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && data != null) {
            Uri selectedImageUri = data.getData();
            Bitmap bitmap = camUtil.getBitmapFromUri(selectedImageUri);

            // Speichern Sie das Bild im internen Speicher
            /*camUtil.saveImageToInternalStorage(bitmap);*/
            profileDialog.getPicture().setImageBitmap(bitmap);
            apiHelper.putProfilePicture(bitmap);


        } else if (requestCode == 1) {
            Bitmap bitmap = camUtil.getBitmapFromFile();
            profileDialog.getPicture().setImageBitmap(bitmap);
            apiHelper.putProfilePicture(bitmap);
        } else if (requestCode == 3 && data != null) {
            Uri selectedImageUri = data.getData();
            Bitmap bitmap = camUtil.getBitmapFromUri(selectedImageUri);

            /*camUtil.saveImageToInternalStorage(bitmap);*/
            reportImageView.setImageBitmap(bitmap);
            reportImageView.setRotation(camUtil.showImage());


        } else if (requestCode == 4) {
            Bitmap bitmap = camUtil.getBitmapFromFile();
            reportImageView.setImageBitmap(bitmap);
            reportImageView.setRotation(camUtil.showImage());
        }
    }

    public static CamUtil getCamUtil() {
        return camUtil;
    }

    public static void setReportImageView(ImageView reportImagePic) {
        reportImageView = reportImagePic;

    }
    public void addToList(ReportModel r){
        allReports.add(r);
    }


}