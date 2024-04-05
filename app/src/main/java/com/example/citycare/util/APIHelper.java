package com.example.citycare.util;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Header;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.citycare.LandingPage;
import com.example.citycare.model.MainCategoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class APIHelper {

    private static volatile APIHelper INSTANCE = null;
    private String registerPostURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/auth/register";
    private String loginPostURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/auth/login";
    private String categoryGetURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/categories/main";
    private String subCategoryGetURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/categories/sub/main/";
    private Context context;
    private RequestQueue requestQueue;
    private Timer timer;
    private String token;
    private SharedPreferences loginSharedPreferences;
    private boolean loggedIn;
    private List<MainCategoryModel> categoryModelList = new ArrayList<>();

    private APIHelper(Context context){
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
        timer = new Timer();
        loginSharedPreferences = context.getSharedPreferences("loggedInOut", Context.MODE_PRIVATE);

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

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    loggedIn =true;
                    updateSharedPreferences();
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
    public void updateSharedPreferences(){
        SharedPreferences.Editor myEditor = loginSharedPreferences.edit();
        myEditor.putBoolean("loggedIn",loggedIn);
        myEditor.apply();

    }

    public List<MainCategoryModel> getMainCategorys() {
        Log.d("catch", token);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, categoryGetURL, null,
                response -> {
                    try {
                        Log.d("catch", "catch");
                        Log.d("MYResponse", String.valueOf(response.length()));
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject categoryObject = response.getJSONArray("").getJSONObject(i);
                            Log.d("MYResponse", categoryObject.toString());
                            MainCategoryModel categoryModel = new MainCategoryModel(
                                    categoryObject.getInt("id"),
                                    categoryObject.getString("title")
                            );
                            categoryModelList.add(categoryModel);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                }
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
        return categoryModelList;
    }

    public List<MainCategoryModel> getAllCategorys() throws JSONException {
        List<MainCategoryModel> allCategory = getMainCategorys();
        for (int i =0;i< allCategory.size();i++){
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("id", allCategory.get(i).getId());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET,subCategoryGetURL+ allCategory.get(i).getId(),jsonBody, jsonObject->{
                        try {
                            JSONArray categoriesArray = jsonObject.getJSONArray("subCategories");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },volleyError -> {
                        volleyError.printStackTrace();
                    }){
                @Override
                public Map<String,String> getHeaders() {
                    HashMap<String,String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer "+ token);
                    return headers;
                }
            };
            requestQueue.add(jsonObjectRequest);
        }



        return allCategory;
    }
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
