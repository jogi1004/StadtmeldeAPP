package com.example.citycare.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;
//TODO Längenüberprüfung Passwort zwischen 6 und 20 Zeichen

public class HelperClass {
    public boolean checkUsername(Context c, String username) {
        if (TextUtils.isEmpty(username.trim())) {
            Toast.makeText(c, "Username darf nicht leer sein", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isDigitsOnly(username.trim())) {
            Toast.makeText(c, "Username darf nicht nur aus Zahlen bestehen", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean checkPassword(Context c,String password) {
        if (TextUtils.isEmpty(password.trim())) {
            Toast.makeText(c, "Passwort darf nicht leer sein", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!hasSpecialCharacter(password)) {
            Toast.makeText(c, "Passwort muss mindestens ein Sonderzeichen enthalten", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public boolean checkPasswordEquality(Context c, String password, String passwordRepeat){
       if(password.equals(passwordRepeat)){
            return true;
       } else {
           Toast.makeText(c, "Die Passwörter stimmen nicht über ein", Toast.LENGTH_SHORT).show();
       }
       return false;
    }

    private boolean hasSpecialCharacter(String password) {
        for (char c : password.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return true;
            }
        }
        return false;
    }
    public boolean checkEmail(Context c, String emailContent){
        if((emailContent.indexOf('@') & emailContent.indexOf('.')) != -1){
            return true;
        } else {
            Toast.makeText(c, "E-Mail-Adresse ist ungültig", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
