package com.example.citycare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.citycare.util.APIHelper;
import com.example.citycare.util.KeyStoreManager;

import org.json.JSONException;

import java.util.Map;

/**
 * Starting Page of App, where user chooses register or sign in
 */
public class WelcomePage extends AppCompatActivity implements View.OnClickListener{
    Button register, signIn;
    private APIHelper apiHelper;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiHelper = APIHelper.getInstance(this);

        try {
            if (checkIfUserExists()) {
                proceedToNextActivity();
                return;
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        setContentView(R.layout.activity_welcome_page);


        register = findViewById(R.id.signUpButton);
        signIn = findViewById(R.id.signInButton);
        register.setOnClickListener(this);
        signIn.setOnClickListener(this);


    }

    private boolean checkIfUserExists() throws JSONException {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("encrypted_password")) {
                username = key.substring("encrypted_password".length());
                return true;
            }
        }
        return false;
    }

    private void proceedToNextActivity() throws JSONException {
        String password = KeyStoreManager.getPassword(this, username);
        apiHelper.loginUser(username, password);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Intent intent = new Intent(this, LandingPage.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v == register){
            Intent i = new Intent(this, RegisterPage.class);
            startActivity(i);
        } else if(v == signIn){
            Intent i = new Intent(this, SignInPage.class);
            startActivity(i);
        }
    }
}