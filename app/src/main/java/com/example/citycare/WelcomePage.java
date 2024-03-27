package com.example.citycare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class WelcomePage extends AppCompatActivity implements View.OnClickListener{
    Button register, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        register = findViewById(R.id.signUpButton);
        signUp = findViewById(R.id.signInButton);
        register.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == register){
            Intent i = new Intent(this, RegisterPage.class);
            startActivity(i);
        } else if(v == signUp){
            Toast.makeText(this, "Anmelden Button gedrückt", Toast.LENGTH_SHORT).show();
        }
    }
}