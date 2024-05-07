package com.example.citycare.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.citycare.Dialogs.PoiInformationDialog;
import com.example.citycare.LandingPage;
import com.example.citycare.R;
import com.example.citycare.model.IconModel;
import com.example.citycare.model.MainCategoryModel;
import com.example.citycare.model.ReportModel;
import com.example.citycare.model.SubCategoryModel;
import com.example.citycare.model.UserModel;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;



import android.location.Address;

public class APIHelper {

    private static volatile APIHelper INSTANCE = null;
    private final String registerPostURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/auth/register";
    private final String loginPostURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/auth/login";
    private final String categoryGetURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/categories/main/location/";
    private final String subCategoryGetURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/categories/sub/main/";
    private final String allReportsURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/reports/location/name/";
    private final String reportPostURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/reports";
    private final String getReportPicture = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/reports/reportPicture/";
    private final String getIconFromLocation = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/icons/location/";
    private final String putProfilPictureURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/user/addProfilePicture";
    private final String getUserInfo = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/user/info";
    private final String getIsLocationMember = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/location/";
    private final String getUserReports = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/reports/user/";
    private final String getProfilePic = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/user/profilePicture/";
    private final String notificationURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/user/notifications";
    private Context context;
    private RequestQueue requestQueue;
    private Timer timer;
    private String token;
    private String username;
    List<String> members = new ArrayList<>();
    List<String> notMembers = new ArrayList<>();
    private UserModel currentUser;
    private final ArrayList<ReportModel> allReports = new ArrayList<>();
    private List<ReportModel> userReports = new ArrayList<>();


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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, registerPostURL, requestBody, jsonObject -> {

                    Intent i = new Intent(context, LandingPage.class);
                    context.startActivity(i);


                }, volleyError -> {
                    int statuscode = volleyError.networkResponse.statusCode;

                    switch (statuscode){
                        case 401:
                            Toast.makeText(context, "Username bereits vorhanden!",Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(context, "Verbindung fehlgeschlagen!",Toast.LENGTH_LONG).show();
                            break;
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    public void loginUser(String username, String password, Callback callback) throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", username);
        requestBody.put("password", password);

        int MY_SOCKET_TIMEOUT_MS = 10000;
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

                    callback.onSuccess();

                }, volleyError -> {
                    callback.onError(volleyError.getMessage());

                    int statuscode = volleyError.networkResponse.statusCode;

                    switch (statuscode){
                        case 403:
                            Toast.makeText(context, "Username oder Passwort falsch!",Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(context, "Verbindung fehlgeschlagen!",Toast.LENGTH_LONG).show();
                            break;

                    }
                }){
            @Override
            public RetryPolicy getRetryPolicy() {
                return new DefaultRetryPolicy(
                        MY_SOCKET_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            }
        };
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
                                    null,
                                    response.getBoolean("notificationsEnabled")
                            );
                        } else{
                            currentUser.setId(response.getInt("id"));
                            currentUser.setUsername(response.getString("username"));
                            currentUser.setEmail(response.getString("email"));
                            currentUser.setNotificationsEnabled(response.getBoolean("notificationsEnabled"));

                        }

                        if (!response.isNull("profilePictureId")) {
                            currentUser.setPicID(response.getInt("profilePictureId"));

                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
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
                                    new IconModel(null, BitmapFactory.decodeResource(context.getResources(),R.drawable.png_placeholder))
                            );
                            if (!jsonObject.isNull("iconId")) {
                                categoryModel.getIcon().setId(jsonObject.getInt("iconId"));
                            }

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
                                if(!jsonObject.getString("title").equals("Sonstiges")){
                                    SubCategoryModel subCategoryModel = new SubCategoryModel(
                                            jsonObject.getInt("maincategoryId"),
                                            jsonObject.getString("title")
                                    );
                                    allSubCategories.add(subCategoryModel);
                                }
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        model.setSubCategorys(allSubCategories);


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
                                    new IconModel(null, null),
                                    jsonObject.getString("timestamp"),
                                    null,
                                    null,
                                    jsonObject.getDouble("longitude"),
                                    jsonObject.getDouble("latitude"),
                                    jsonObject.getString("description")
                            );

                            if (!jsonObject.isNull("reportPictureId")) {
                                reportModel.setImageId(jsonObject.getInt("reportPictureId"));
                            }

                            if (!jsonObject.isNull("iconId")) {
                                reportModel.getIcon().setId(jsonObject.getInt("iconId"));
                            }

                            allReports.add(reportModel);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    callback.onSuccess(allReports);
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000000,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);



    }

    public void getReportPic(ReportModel model, final BitmapCallback callback) {
        if(model.getImageId() != null) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET, getReportPicture + model.getImageId(), null,
                    response -> {
                        try {
                            if (!response.isNull("picture")) {
                                Bitmap image = decodeImage(Base64.decode(response.getString("picture"), Base64.DEFAULT));
                                model.setImage(image);
                                callback.onBitmapLoaded(model);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onBitmapError(e);
                        }
                    },
                    error -> {
                        error.printStackTrace();
                        callback.onBitmapError(new Exception("Error fetching report picture"));
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };
            requestQueue.add(jsonObjectRequest);
        }
    }

    public void getIconFromLocation(String location, final BitmapCallback callback) {

        List<IconModel> icons = new ArrayList<>();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, getIconFromLocation + location, null,
                response -> {
                    for (int i=0;i<response.length();i++) {
                        IconModel iconModel = new IconModel(null, null);
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            if (!jsonObject.isNull("icon")) {
                                Bitmap icon = decodeImage(Base64.decode(jsonObject.getString("icon"), Base64.DEFAULT));
                                iconModel = new IconModel(jsonObject.getInt("id"), icon);
                            }
                            if(iconModel.getId() != null){
                                icons.add(iconModel);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onBitmapError(e);
                        }
                    }
                    callback.onBitmapLoaded(icons);
                },
                error -> {
                    error.printStackTrace();
                    callback.onBitmapError(new Exception("Error fetching icon"));
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public interface BitmapCallback <T>{
        void onBitmapLoaded(T model);
        void onBitmapError(Exception e);
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

        if (report.getImage()!=null){
            requestBody.put("additionalPicture", Base64.encodeToString(encodeImage(report.getImage()), Base64.DEFAULT));
        } else{
            requestBody.put("additionalPicture", null);
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, reportPostURL, requestBody, jsonObject -> {
                    LandingPage.setMarker(report);
                }, volleyError -> {
                    volleyError.printStackTrace();
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

    public void getIsLocationMember(GeoPoint geoPoint, LandingPage landingPage, PoiInformationDialog poiInformationDialog){

        String location;

        Geocoder geocoder = new Geocoder(landingPage);
        try {
            List<Address> addresses = geocoder.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
            assert addresses != null;
            location = addresses.get(0).getLocality();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        StringRequest booleanRequest = new StringRequest(Request.Method.GET, getIsLocationMember + location,
                response -> {
                    boolean isMember = Boolean.parseBoolean(response);

                    if (isMember) {
                        poiInformationDialog.setGifVisibility(View.GONE);
                        poiInformationDialog.setButtonVisibility(View.VISIBLE);
                        members.add(location);
                    } else {
                        poiInformationDialog.setGifVisibility(View.GONE);
                        poiInformationDialog.setButtonVisibility(View.INVISIBLE);
                        Toast.makeText(poiInformationDialog.getContext(), "Stadt ist kein Mitglied", Toast.LENGTH_LONG).show();
                        notMembers.add(location);
                    }
                },
                volleyError -> {
                    volleyError.printStackTrace();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        requestQueue.add(booleanRequest);

    }

    public void getUserReports(AllReportsCallback callback){

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET,getUserReports + currentUser.getId(),null,
                response->{
                    for (int i=0;i<response.length();i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            ReportModel report = new ReportModel(
                                    jsonObject.getString("titleOrsubcategoryName"),
                                    new IconModel(null, null),
                                    jsonObject.getString("timestamp"),
                                    null,
                                    null,
                                    jsonObject.getDouble("longitude"),
                                    jsonObject.getDouble("latitude"),
                                    jsonObject.getString("description")
                            );

                            if (!jsonObject.isNull("iconId")) {
                                report.getIcon().setId(jsonObject.getInt("iconId"));
                            }

                            if (!jsonObject.isNull("reportPictureId")){
                                report.setImageId(jsonObject.getInt("reportPictureId"));
                            }
                            userReports.add(report);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    callback.onSuccess(userReports);
                },volleyerror->{
            volleyerror.printStackTrace();
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);

    }

    public void getProfilePic(Callback picCallback) {
        JSONObject body = new JSONObject();
        try {
            body.put("id", currentUser.getPicID());

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getProfilePic + currentUser.getPicID(), body,
                response -> {
                    try {
                        currentUser.setProfilePicture(decodeImage(Base64.decode(response.getString("profilePicture"), Base64.DEFAULT)));
                        picCallback.onSuccess();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }, volleyError -> {

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }


    public void setEmailNotification(boolean enable){
        JSONObject j = new JSONObject();
        try {
            j.put("notification", enable);
        }catch(JSONException e){
            Log.e("JSON", "Error", e);
        }
        JsonObjectRequest jOr = new JsonObjectRequest(
                Request.Method.PUT, notificationURL,j,null, volleyError -> volleyError.printStackTrace()
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        requestQueue.add(jOr);
    }


    public List<String> getMembers() {
        return members;
    }

    public List<String> getNotMembers() {
        return notMembers;
    }

    public ArrayList<ReportModel> getAllReportsAsList() {
        return allReports;
    }

    public UserModel getCurrentUser() {
        return currentUser;
    }

}
