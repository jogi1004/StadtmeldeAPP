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

public class RegisterPage extends AppCompatActivity implements View.OnClickListener {

    ImageButton backButton;
    Button SignIn, register;
    EditText username, password, passwordRepeat;
    boolean SignUpDataCorrect = false;
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
            SignUpDataCorrect = checkSignUpData(usernameContent, passwordContent, passwordRepeatContent);
            if (SignUpDataCorrect) {
                Intent i = new Intent(this, LandingPage.class);
                startActivity(i);
            } else {
                Toast.makeText(this, "Fülle bitte alle Felder aus!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private boolean checkSignUpData(String usernameContent, String passwordContent, String passwordRepeatContent) {
        return !TextUtils.isEmpty(usernameContent.trim()) && !TextUtils.isEmpty(passwordContent.trim()) && !TextUtils.isEmpty(passwordRepeatContent.trim()) && passwordContent.contentEquals(passwordRepeatContent);
    }
}