package com.example.citycare;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignInPage extends AppCompatActivity implements View.OnClickListener {
    TextView registerView;
    Button SignInButton;
    ImageButton backButton;
    EditText username, password;
    boolean checkInCorrect = false;
    String usernameContent, passwordContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_layout);

        //Zum Anmelden erforderliche Objekte initialisieren
        SignInButton = findViewById(R.id.SignUpButton);
        registerView = findViewById(R.id.RegisterButtonView);
        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
        backButton = findViewById(R.id.backButton);

        // OnClickListener auf alle Buttons setzen
        SignInButton.setOnClickListener(this);
        registerView.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == SignInButton){
            usernameContent = String.valueOf(username.getText());
            Log.d("CC",usernameContent +" ");
            passwordContent = String.valueOf(password.getText());
            Log.d("CC",passwordContent + " ");
            checkInCorrect = checkLoginData(usernameContent, passwordContent);
            Log.d("CC", String.valueOf(checkInCorrect));
            if(checkInCorrect){
                Intent i = new Intent(this, LandingPage.class);
                startActivity(i);
            } else{
                Toast.makeText(this, "Fülle zuerst alle Felder aus!", Toast.LENGTH_SHORT).show();
            }
        } else if(v == registerView){
            Intent i = new Intent(this, RegisterPage.class);
            startActivity(i);
        } else if(v == backButton){
            Intent i = new Intent(this, WelcomePage.class);
            startActivity(i);
        }
    }

    private boolean checkLoginData(String usernameContent, String passwordContent) {
        return !TextUtils.isEmpty(usernameContent.trim()) && !TextUtils.isEmpty(passwordContent.trim());
    }
}
