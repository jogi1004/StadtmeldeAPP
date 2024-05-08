package com.example.citycare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * sign in Page requires username and password
 */

import androidx.appcompat.app.AppCompatActivity;

import com.example.citycare.util.APIHelper;
import com.example.citycare.util.Callback;
import com.example.citycare.util.HelperClass;

import org.json.JSONException;

public class SignInPage extends AppCompatActivity implements View.OnClickListener {
    HelperClass h = new HelperClass();
    TextView registerView;
    Button SignInButton;
    ImageButton backButton;
    EditText username, password;
    String usernameContent, passwordContent;
    private APIHelper apiHelper;

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

        apiHelper = APIHelper.getInstance(this);



    }

    @Override
    public void onClick(View v) {
        if(v == SignInButton){
            usernameContent = String.valueOf(username.getText());
            passwordContent = String.valueOf(password.getText());
            if(checkLoginData(usernameContent, passwordContent)){
                try {
                    apiHelper.loginUser(usernameContent.trim(), passwordContent.trim(), new Callback() {
                        @Override
                        public void onSuccess() {
                            Intent i = new Intent(getBaseContext(), LandingPage.class);
                            startActivity(i);
                        }

                        @Override
                        public void onError(String errorMessage) {

                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
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
        return h.checkUsername(this,usernameContent) && h.checkPassword(this, passwordContent);
    }

}
