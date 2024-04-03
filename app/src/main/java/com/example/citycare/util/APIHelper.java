package com.example.citycare.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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
        requestBody.put("username", username);
        requestBody.put("email", email);
        requestBody.put("password", password);

        Log.d("registerdb", requestBody.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, postURL, requestBody, jsonObject -> {
                    try {
                        String usernameJSON = jsonObject.getString("username");
                        Log.d("registerdb", usernameJSON);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                }, volleyError -> {

                });
        requestQueue.add(jsonObjectRequest);
    }

}
