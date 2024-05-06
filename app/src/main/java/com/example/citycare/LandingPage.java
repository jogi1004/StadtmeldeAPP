package com.example.citycare;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.citycare.Dialogs.PoiDialog;
import com.example.citycare.Dialogs.PoiInformationDialog;
import com.example.citycare.Dialogs.ProfilDialog;
import com.example.citycare.Dialogs.SearchDialog;
import com.example.citycare.Dialogs.fragment_damagetype;
import com.example.citycare.Dialogs.ReportDialogPage;
import com.example.citycare.Dialogs.SettingDialog;
import com.example.citycare.FAB.MyFloatingActionButtons;
import com.example.citycare.adapter.RecyclerViewAdapter_AllReports;
import com.example.citycare.model.IconModel;
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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class LandingPage extends AppCompatActivity implements MapListener, View.OnClickListener {

    private static final String PREF_LASTADRESSES = "lastAdresses";
    private static MapView mMap;
    private Marker poiMarker;
    private IMapController controller;
    private MyLocationNewOverlay mMyLocationOverlay;
    public FrameLayout dimm;
    public ProfilDialog profileDialog;
    public PoiInformationDialog poiInformationDialog;
    private SearchDialog searchDialog;
    private ReportDialogPage allReportsDialog;
    private static APIHelper apiHelper;
    private static List<MainCategoryModel> mainCategoryList = new ArrayList<>();
    private static List<IconModel> iconsFromLocationList = new ArrayList<>();
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
    private static RecyclerViewAdapter_AllReports adapterReportList;

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

        adapterReportList = new RecyclerViewAdapter_AllReports(context, allReports);

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

        initPermissions();
        poiInformationDialog = new PoiInformationDialog(this, this, getSupportFragmentManager());

        profileDialog = new ProfilDialog(this, this, camUtil);
        allReportsDialog = new ReportDialogPage(this, this,adapterReportList );
        SettingDialog settingDialog = new SettingDialog(this);
        searchDialog = new SearchDialog(this, this, poiInformationDialog);

        searchDialog.setOnShowListener(dialogInterface -> {
            SearchDialog searchDialog = (SearchDialog) dialogInterface;

            SharedPreferences SPLastAdresses = context.getSharedPreferences(PREF_LASTADRESSES, Context.MODE_PRIVATE);
            Map<String, ?> allEntries = SPLastAdresses.getAll();

            TextView address1 = searchDialog.findViewById(R.id.adress1);
            TextView address2 = searchDialog.findViewById(R.id.adress2);
            TextView address3 = searchDialog.findViewById(R.id.adress3);

            ConstraintLayout layout1 = searchDialog.findViewById(R.id.layout1);
            ConstraintLayout layout2 = searchDialog.findViewById(R.id.layout2);
            ConstraintLayout layout3 = searchDialog.findViewById(R.id.layout3);

            if(!allEntries.isEmpty()){
                int i = 0;
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    if (i < 3) {
                        String prompt = (String) entry.getValue();
                        String[] teile = prompt.split(",");

                        switch (i) {
                            case 0:
                                address1.setText(teile[0]);
                                break;
                            case 1:
                                address2.setText(teile[0]);
                                break;
                            case 2:
                                address3.setText(teile[0]);
                                break;
                            default:
                                break;
                        }
                        i++;
                    }
                }
            }

            if (SPLastAdresses.contains("LastAddresses1")){
                layout1.setVisibility(View.VISIBLE);
                layout1.setOnClickListener(v ->{
                    searchDialog.performSearch(address1.getText().toString());
                });
            }
            if (SPLastAdresses.contains("LastAddresses2")){
                layout2.setVisibility(View.VISIBLE);
                layout2.setOnClickListener(v ->{
                    searchDialog.performSearch(address2.getText().toString());
                });
            }
            if (SPLastAdresses.contains("LastAddresses3")){
                layout3.setVisibility(View.VISIBLE);
                layout3.setOnClickListener(v -> {
                    searchDialog.performSearch(address3.getText().toString());
                });
            }

        });
        new MyFloatingActionButtons(this, this, false, profileDialog, settingDialog, allReportsDialog, poiInformationDialog, searchDialog);
        myFloatingActionButtons = new MyFloatingActionButtons(this, this, false, profileDialog, settingDialog, allReportsDialog, poiInformationDialog, searchDialog);
    }

    public static MyFloatingActionButtons getMyFloatingActionButtons() {
        return myFloatingActionButtons;
    }


    protected void initPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            loadMap();
            updateLocation();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            initPermissions();
        }
    }

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

    private void fillListWithData(String cityName) {
        apiHelper.getMainCategorys(cityName, new CategoryListCallback() {

            @Override
            public void onSuccess(List<MainCategoryModel> categoryModels) {
                mainCategoryList.clear();
                mainCategoryList = categoryModels;
                isMember = true;


                if (fragment_damagetype.adapter != null && !categoryModels.isEmpty()) {
                    fragment_damagetype.adapter.setData(mainCategoryList);
                }

                for (MainCategoryModel mainCategory: mainCategoryList) {
                    if(mainCategory.getIcon().getId() != null){
                        for (IconModel iconModel: iconsFromLocationList) {
                            if (iconModel.getId() == mainCategory.getIcon().getId()){
                                Log.d("icons", "Ist drin");
                                mainCategory.getIcon().setIcon(iconModel.getIcon());
                            }
                        }
                    }
                }
                adapterReportList.notifyDataSetChanged();

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

    @SuppressLint("MissingPermission")
    protected void updateLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this::onLocationReceived);
    }

    protected void onLocationReceived(Location location) {
        if (!alreadyCalled) {
            alreadyCalled = true;
            Toast.makeText(this, "Aktualisiere Meldungen...", Toast.LENGTH_LONG).show();
            loadIconsFromLocation(location);
            loadListfromDB(location);
        }
    }

    public void loadIconsFromLocation(Location location) {
        Log.d("icons", "loadIconsFromLocation");
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assert addresses != null;
        String cityName = addresses.get(0).getLocality();

        apiHelper.getIconFromLocation(cityName, new APIHelper.BitmapCallback <List<IconModel>>() {
            @Override
            public void onBitmapLoaded(List<IconModel> icons) {
                Log.d("icons", "Alle icons: " + icons.toString());
                iconsFromLocationList = icons;
            }

            @Override
            public void onBitmapError(Exception e) {
            }
        });
    }

    public static List<IconModel> getIconsFromLocationList() {
        return iconsFromLocationList;
    }

    public static void setIconsFromLocationList(List<IconModel> iconsFromLocationList) {
        LandingPage.iconsFromLocationList = iconsFromLocationList;
    }

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

                        adapterReportList.updateList(allReports);

                        loadIconsForReports(allReports);

                        loadExistingMarkers();
                        Log.d("updateallReportsLandingSP", String.valueOf(allReports.size()));
                        adapterReportList.updateList(allReports);
                        for (ReportModel m: reports) {
                            if (m.getImageId()!=null){
                                apiHelper.getReportPic(m, new APIHelper.BitmapCallback <ReportModel>() {
                                    @Override
                                    public void onBitmapLoaded(ReportModel model) {
                                    }

                                    @Override
                                    public void onBitmapError(Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.e("Error in checkingListsOfEquality: ", errorMessage);
                    }
                });

            } else {

                apiHelper.getAllReports(cityName, new AllReportsCallback() {
                    @Override
                    public void onSuccess(List<ReportModel> reports) {
                        allReports = reports;
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(allReports);
                        editor.putString(KEY_REPORT_LIST, json);
                        editor.apply();
                        alreadyCalled = true;
                        loadExistingMarkers();

                        for (ReportModel m: allReports) {
                            if (m.getImageId()!=null) {
                                apiHelper.getReportPic(m, new APIHelper.BitmapCallback <ReportModel>() {
                                    @Override
                                    public void onBitmapLoaded(ReportModel model) {

                                    }

                                    @Override
                                    public void onBitmapError(Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.e("Error in onLocationReceived: ", errorMessage);
                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadIconsForReports(List<ReportModel> allReports) {
        for (ReportModel report: allReports) {
            for (IconModel iconModel: iconsFromLocationList) {
                if (Objects.equals(iconModel.getId(), report.getIcon().getId())){
                    report.getIcon().setIcon(iconModel.getIcon());
                }
            }
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

    public static void loadExistingMarkers() {
        for (ReportModel m : allReports) {
            setMarker(m);
        }
    }

    public static void setMarker(ReportModel m) {
        Marker poi = new Marker(mMap);
        GeoPoint geoP = new GeoPoint(m.getLatitude(), m.getLongitude());
        poi.setPosition(geoP);
        poi.setIcon(ContextCompat.getDrawable(context, R.drawable.png_poi));
        List<Marker> markers = new ArrayList<>();


        poi.setOnMarkerClickListener((marker, mapView) -> {
            PoiDialog poiDialog = new PoiDialog(context,m);
            if(!markers.contains(marker)) {
                if(m.getImageId() != null) {
                    poiDialog.existsImage(true);
                    apiHelper.getReportPic(m, new APIHelper.BitmapCallback <ReportModel>() {
                        @Override
                        public void onBitmapLoaded(ReportModel model) {
                            poiDialog.updateImage(model.getImage());

                        }

                        @Override
                        public void onBitmapError(Exception e) {
                            Log.e("onBitmapError", e.toString());
                            Toast.makeText(context, "Bild wurde nicht korrekt geladen", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    poiDialog.existsImage(false);
                }
            }
            poiDialog.show();
            markers.add(marker);
            myFloatingActionButtons.hideFABS();
            return true;
        });

        mMap.getOverlays().add(poi);
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

    public static void setAllReports(List<ReportModel> allReports) {
        LandingPage.allReports = allReports;
        adapterReportList.updateList(allReports);
        loadExistingMarkers();
    }
    public static RecyclerViewAdapter_AllReports getAdapterReportList() {
        return adapterReportList;
    }
}