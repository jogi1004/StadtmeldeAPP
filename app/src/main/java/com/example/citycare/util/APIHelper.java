package com.example.citycare.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.citycare.LandingPage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class APIHelper {
    private String registerPostURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/auth/register";
    private String loginPostURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/auth/login";
    private Context context;
    private RequestQueue requestQueue;
    private Timer timer;
    private String token;
    private SharedPreferences loginSharedPreferences;
    private boolean loggedIn;

    public APIHelper(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
        timer = new Timer();

        loginSharedPreferences = context.getSharedPreferences("loggedInOut", Context.MODE_PRIVATE);
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

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
