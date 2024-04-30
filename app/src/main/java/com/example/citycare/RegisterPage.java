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
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.citycare.util.APIHelper;
import com.example.citycare.util.HelperClass;

import org.json.JSONException;

/**
 * RegisterPage for register with email,username and password
 */
public class RegisterPage extends AppCompatActivity implements View.OnClickListener {

    HelperClass h = new HelperClass();
    ImageButton backButton;
    Button SignIn, register;
    EditText username, password, passwordRepeat, email;
    String usernameContent, passwordContent, passwordRepeatContent, emailContent;
    ConstraintLayout GoogleRegister;

    private APIHelper apiHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiHelper = APIHelper.getInstance(this);
        setContentView(R.layout.activity_register_page);
        //Für die Registrierung benötigte Objekte initialisieren
        backButton = findViewById(R.id.backButton);
        SignIn = findViewById(R.id.SignInButtonRegisterView);
        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
        email = findViewById(R.id.emailEditText);
        passwordRepeat = findViewById(R.id.passwordRepeatEditText);
        register = findViewById(R.id.RegisterButton);
        GoogleRegister = findViewById(R.id.GoogleRegisterLayout);
        //OnClickListener auf Buttons setzen
        backButton.setOnClickListener(this);
        SignIn.setOnClickListener(this);
        register.setOnClickListener(this);
        GoogleRegister.setOnClickListener(this);
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
            emailContent = String.valueOf(email.getText());
            passwordContent = String.valueOf(password.getText());
            passwordRepeatContent = String.valueOf(passwordRepeat.getText());
            if (checkSignUpData(usernameContent, passwordContent, passwordRepeatContent, emailContent)) {
                try {
                    apiHelper.registerUser(usernameContent.trim(),emailContent.trim(),passwordContent.trim());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        } else if(v == GoogleRegister){
            Toast.makeText(this, "Mit Google registrieren", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean checkSignUpData(String usernameContent, String passwordContent, String passwordRepeatContent, String emailContent) {
        return h.checkUsername(this, usernameContent) && h.checkPassword(this, passwordContent) && h.checkPasswordEquality(this,passwordContent, passwordRepeatContent) && h.checkEmail(this, emailContent);
    }
}