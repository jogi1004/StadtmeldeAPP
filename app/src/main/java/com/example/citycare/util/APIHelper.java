package com.example.citycare.util;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class APIHelper {
    private String postURL = "https://backendservice-dev-5rt6jcn4da-uc.a.run.app/auth/register";

    public void registerUser(String username, String email, String password){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, postURL, null, new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject jsonObject) {


                    }
                }
    }

}
