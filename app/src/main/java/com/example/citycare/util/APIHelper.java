package com.example.citycare.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.citycare.LandingPage;
import com.example.citycare.R;
import com.example.citycare.model.MainCategoryModel;
import com.example.citycare.model.ReportModel;
import com.example.citycare.model.SubCategoryModel;
import com.example.citycare.model.UserModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;


import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import android.location.Address;


public class APIHelper {

    private static volatile APIHelper INSTANCE = null;
    private final String registerPostURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/auth/register";
    private final String loginPostURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/auth/login";
    private final String categoryGetURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/categories/main/location/";
    private final String subCategoryGetURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/categories/sub/main/";
    private final String allReportsURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/reports/location/name/";
    private final String reportPostURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/reports";
    private final String putProfilPictureURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/user/addProfilePicture";
    private final String getUserInfo = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/user/info";
    private final String getIsLocationMember = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/location/";
    private Context context;
    private RequestQueue requestQueue;
    private Timer timer;
    private String token;
    private String username;
    private UserModel currentUser;
    private ArrayList<ReportModel> allReports = new ArrayList<>();

    private APIHelper(Context context){
        this.context = context;
        requestQueue = getRequestQueue();
        timer = new Timer();
    }
    public static APIHelper getInstance(Context context){
        if(INSTANCE==null){
            synchronized (APIHelper.class){
                if(INSTANCE==null){
                    INSTANCE = new APIHelper(context);
                }
            }
        } else {

        }
        return INSTANCE;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
    public void registerUser(String username, String email, String password) throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", username);
        requestBody.put("email", email);
        requestBody.put("password", password);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Timeout-Verhalten implementieren (z.B. Fehlermeldung anzeigen)
                Toast.makeText(context, "Registrierung fehlgeschlagen - Zeitüberschreitung", Toast.LENGTH_LONG).show();
            }
        }, 2000);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, registerPostURL, requestBody, jsonObject -> {
                    timer.cancel();
                    Intent i = new Intent(context, LandingPage.class);
                    context.startActivity(i);


                }, volleyError -> {
                    int statuscode = volleyError.networkResponse.statusCode;

                    switch (statuscode){
                        case 401:
                            Toast.makeText(context, "Username bereits vorhanden!",Toast.LENGTH_LONG).show();
                            timer.cancel();
                            break;
                        default:
                            Toast.makeText(context, "Verbindung fehlgeschlagen!",Toast.LENGTH_LONG).show();
                            timer.cancel();
                            break;

                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    public void loginUser(String username, String password) throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", username);
        requestBody.put("password", password);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, loginPostURL, requestBody, jsonObject -> {
                    try {
                        token = jsonObject.getString("token");
                        getUserInfo();
                        this.username = username;
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    KeyStoreManager.savePassword(context, username, password);

                    Intent i = new Intent(context, LandingPage.class);
                    context.startActivity(i);

                }, volleyError -> {
                    int statuscode = volleyError.networkResponse.statusCode;

                    switch (statuscode){
                        case 403:
                            Toast.makeText(context, "Username oder Passwort falsch!",Toast.LENGTH_LONG).show();
                            timer.cancel();
                            break;
                        default:
                            Toast.makeText(context, "Verbindung fehlgeschlagen!",Toast.LENGTH_LONG).show();
                            timer.cancel();
                            break;

                    }
                });
        requestQueue.add(jsonObjectRequest);
    }


    public void putProfilePicture(Bitmap bitmap) {
        byte[] imagesBytes = encodeImage(bitmap);
        JSONObject body = new JSONObject();
        try {
            body.put("profilePicture", Base64.encodeToString(imagesBytes, Base64.DEFAULT));
        } catch (JSONException e) {
            Log.e("putProfilePicture", "Error", e);
            return;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, putProfilPictureURL, body,
                response -> {
                },
                volleyError -> {
                    volleyError.printStackTrace();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                Log.d("imagesBytePUT", headers.get("Authorization"));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public void getUserInfo(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,getUserInfo,null,
                response->{
                    try {
                        if (currentUser==null) {
                            currentUser = new UserModel(
                                    response.getInt("id"),
                                    response.getString("username"),
                                    response.getString("email"),
                                    decodeImage(Base64.decode(response.getString("profilePicture"), Base64.DEFAULT)),
                                    response.getBoolean("notificationsEnabled")
                            );
                        } else{
                            currentUser.setId(response.getInt("id"));
                            currentUser.setUsername(response.getString("username"));
                            currentUser.setEmail(response.getString("email"));
                            currentUser.setProfilePicture(decodeImage(Base64.decode(response.getString("profilePicture"), Base64.DEFAULT)));
                            currentUser.setNotificationsEnabled(response.getBoolean("notificationsEnabled"));

                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                Log.d("userDATA", currentUser.toString());
                }, volleyError -> volleyError.printStackTrace())
                {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "Bearer "+token);
                        return headers;
                    }
                };
        requestQueue.add(jsonObjectRequest);
    }

    public byte[] encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    public Bitmap decodeImage(byte[] imageBytes) {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }
    public void getMainCategorys(String cityName, CategoryListCallback callback) {
        List<MainCategoryModel> categoryModelList = new ArrayList<>();

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, categoryGetURL + cityName, null,
                response -> {
                    for (int i=0;i<response.length();i++){
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            MainCategoryModel categoryModel = new MainCategoryModel(
                                    jsonObject.getInt("id"),
                                    jsonObject.getString("title"),
                                    R.drawable.png_placeholder
                            );

                            categoryModelList.add(categoryModel);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    callback.onSuccess(categoryModelList);
                },
                error -> error.printStackTrace()
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }
        };


        requestQueue.add(jsonObjectRequest);
    }


    public void putSubCategories(CategoryListCallback callback, List<MainCategoryModel> mainCategories) {

        int tmp = 0;
        for (MainCategoryModel model : mainCategories) {
            List<SubCategoryModel> allSubCategories = new ArrayList<>();
            int finalI = tmp;
            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                    (Request.Method.GET, subCategoryGetURL + model.getId(), null, response -> {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                SubCategoryModel subCategoryModel = new SubCategoryModel(
                                        jsonObject.getInt("id"),
                                        jsonObject.getString("title")
                                );
                                allSubCategories.add(subCategoryModel);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        model.setSubCategorys(allSubCategories);
                        Log.d("Subs", "Ganzes Model " + model);


                        if (finalI == mainCategories.size() - 1) {
                            callback.onSuccess(mainCategories);
                        }
                    }, volleyError -> volleyError.printStackTrace()) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };
            requestQueue.add(jsonObjectRequest);
            tmp++;
        }
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void getAllReports(String cityName, AllReportsCallback callback) {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, allReportsURL + cityName, null,
                response -> {
                    for (int i=0;i<response.length();i++){
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            ReportModel reportModel = new ReportModel(
                                    jsonObject.getString("titleOrsubcategoryName"),
                                    null,
                                    jsonObject.getString("timestamp"),
                                    null,
                                    jsonObject.getDouble("longitude"),
                                    jsonObject.getDouble("latitude")
                                    );
                            allReports.add(reportModel);
                            Log.d("allReports", reportModel.toString() + "\n " + allReports.size());
                            callback.onSuccess();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                error -> error.printStackTrace()
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public void postReport(ReportModel report) throws JSONException {

        JSONObject requestBody = new JSONObject();
        requestBody.put("title", report.getTitle());
        requestBody.put("subCategoryName", report.getSubCategory());
        requestBody.put("mainCategoryName", report.getMainCategory());
        requestBody.put("description", report.getDescription());
        requestBody.put("longitude", report.getLongitude());
        requestBody.put("latitude", report.getLatitude());
        requestBody.put("reportingLocationName", report.getLocationName());
        requestBody.put("additionalPicture", report.getImage());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, reportPostURL, requestBody, jsonObject -> {
                    LandingPage.setMarker(report, context);
                }, volleyError -> {
                    int statuscode = volleyError.networkResponse.statusCode;

                    if (statuscode == 404) {
                        Toast.makeText(context, "Fehler bei den Daten!", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public void getIsLocationMember(GeoPoint geoPoint, LandingPage landingPage){

        String location;

        Geocoder geocoder = new Geocoder(landingPage);
        try {
            List<Address> addresses = geocoder.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
            assert addresses != null;
            location = addresses.get(0).getLocality();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Log.d("Bug", location);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,getIsLocationMember + location,null,
                response->{
                    Log.d("Member", "true");
                    landingPage.updatePoiMarker(new GeoPoint(geoPoint.getLatitude(), geoPoint.getLongitude()), true);
                }, volleyError -> {
            landingPage.updatePoiMarker(new GeoPoint(geoPoint.getLatitude(), geoPoint.getLongitude()), false);
            Log.d("Member", "false");
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }


    public ArrayList<ReportModel> getAllReportsAsList() {
        return allReports;
    }

    public UserModel getCurrentUser() {
        return currentUser;
    }

}
