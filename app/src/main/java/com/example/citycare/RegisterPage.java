package com.example.citycare;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.citycare.util.HelperClass;

public class RegisterPage extends AppCompatActivity implements View.OnClickListener {

    HelperClass h = new HelperClass();
    ImageButton backButton;
    Button SignIn, register;
    EditText username, password, passwordRepeat;
    String usernameContent, passwordContent, passwordRepeatContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        //Für die Registrierung benötigte Objekte initialisieren
        backButton = findViewById(R.id.backButton);
        SignIn = findViewById(R.id.SignInButtonRegisterView);
        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
        passwordRepeat = findViewById(R.id.passwordRepeatEditText);
        register = findViewById(R.id.RegisterButton);
        //OnClickListener auf Buttons setzen
        backButton.setOnClickListener(this);
        SignIn.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == backButton) {
            Intent i = new Intent(this, WelcomePage.class);
            startActivity(i);
        } else if (v == SignIn) {
            Intent i = new Intent(this, SignInPage.class);
            startActivity(i);
        } else if (v == register) {
            usernameContent = String.valueOf(username.getText());
            passwordContent = String.valueOf(password.getText());
            passwordRepeatContent = String.valueOf(passwordRepeat.getText());
            if (checkSignUpData(usernameContent, passwordContent, passwordRepeatContent)) {
                Intent i = new Intent(this, LandingPage.class);
                startActivity(i);
            }
        }
    }


    private boolean checkSignUpData(String usernameContent, String passwordContent, String passwordRepeatContent) {
        return h.checkUsername(this, usernameContent) && h.checkPassword(this, passwordContent) && h.checkPasswordEquality(this,passwordContent, passwordRepeatContent);
    }
}