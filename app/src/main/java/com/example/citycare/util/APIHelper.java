package com.example.citycare.util;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.citycare.LandingPage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class APIHelper {
    private String postURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/auth/register";
    private Context context;
    private RequestQueue requestQueue;

    public APIHelper(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void registerUser(String username, String email, String password) throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", username.trim());
        requestBody.put("email", email.trim());
        requestBody.put("password", password.trim());
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Timeout-Verhalten implementieren (z.B. Fehlermeldung anzeigen)
                Toast.makeText(context, "Registrierung fehlgeschlagen - Zeitüberschreitung", Toast.LENGTH_LONG).show();
            }
        }, 2000);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, postURL, requestBody, jsonObject -> {
                    timer.cancel();
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

}
