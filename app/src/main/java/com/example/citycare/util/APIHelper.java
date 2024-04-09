package com.example.citycare.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.citycare.LandingPage;
import com.example.citycare.model.MainCategoryModel;
import com.example.citycare.model.SubCategoryModel;
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
    private String categoryGetURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/categories/main/location/Zweibrücken";
    private String subCategoryGetURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/categories/sub/main/";
    private Context context;
    private RequestQueue requestQueue;
    private Timer timer;
    private String token;

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


    public void getMainCategorys(CategoryListCallback callback) {
        List<MainCategoryModel> categoryModelList = new ArrayList<>();

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, categoryGetURL, null,
                response -> {
                    for (int i=0;i<response.length();i++){
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            MainCategoryModel categoryModel = new MainCategoryModel(
                                    jsonObject.getInt("id"),
                                    jsonObject.getString("title")
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


    public void getAllCategorys(CategoryListCallback callback){
        getMainCategorys(new CategoryListCallback() {
            @Override
            public void onSuccess(List<MainCategoryModel> categoryModels) {
                /*Log.d("catch", String.valueOf(categoryModels.size()));*/
                    for (int i=0;i< categoryModels.size();i++) {
                    int finalI = i;
                    RequestFuture<JsonArrayRequest> future = RequestFuture.newFuture();
                    JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                            (Request.Method.GET, subCategoryGetURL + categoryModels.get(i).getId(), null, response -> {
                                List<SubCategoryModel> allSubCategories = new ArrayList<>();
                                try {
                                    for (int j = 0; j < response.length(); j++) {
                                        JSONObject jsonObject = response.getJSONObject(j);
                                        SubCategoryModel subCategoryModel = new SubCategoryModel(
                                                jsonObject.getInt("id"),
                                                jsonObject.getString("title")
                                        );
                                        allSubCategories.add(subCategoryModel);
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
//                                Log.d("catchi", String.valueOf(allSubCategories.size()));
                                categoryModels.get(finalI).setSubCategorys(allSubCategories);
//                                Log.d("catchu" , categoryModels.get(finalI).toString());

                                if(finalI == categoryModels.size()-1) {
                                    callback.onSuccess(categoryModels);
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
                }
//                callback.onSuccess(categoryModels);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("CallBackError", errorMessage);
            }
        });
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
