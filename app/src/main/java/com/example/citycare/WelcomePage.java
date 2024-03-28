package com.example.citycare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class WelcomePage extends AppCompatActivity implements View.OnClickListener{
    Button register, signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        register = findViewById(R.id.signUpButton);
        signIn = findViewById(R.id.signInButton);
        register.setOnClickListener(this);
        signIn.setOnClickListener(this);
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