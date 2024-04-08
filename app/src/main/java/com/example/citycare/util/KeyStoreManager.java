package com.example.citycare.util;
import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeyStoreManager {

    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    private static final String KEY_ALIAS = "my_secret_key";

    private static final String PREFS_NAME = "MyAppPrefs";

    private static final String ENCRYPTED_PASSWORD_KEY = "encrypted_password";

    public static void savePassword(Context context, String username, String password) {
        try {
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
            keyStore.load(null);

            // Überprüfen, ob der Schlüssel bereits vorhanden ist
            if (!keyStore.containsAlias(KEY_ALIAS)) {
                // Generieren Sie einen neuen Schlüssel, wenn er noch "nicht" vorhanden ist
                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE);
                keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build());
                SecretKey secretKey = keyGenerator.generateKey();
            }

            // Jetzt verschlüsseln wir das Passwort und speichern es im KeyStore
            KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null);
            SecretKey secretKey = secretKeyEntry.getSecretKey();

            byte[] encryptedPassword = AESUtils.encrypt(password, secretKey);

            // Speichern Sie das verschlüsselte Passwort in SharedPreferences oder einer anderen sicheren Speicherungsmethode
            // In diesem Beispiel verwenden wir SharedPreferences
            saveEncryptedPasswordToSharedPreferences(context, username, encryptedPassword);

        } catch (IOException | InvalidAlgorithmParameterException | UnrecoverableEntryException |
                 CertificateException | KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
            // Fehlerbehandlung
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    private static void saveEncryptedPasswordToSharedPreferences(Context context, String username, byte[] encryptedPassword) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(ENCRYPTED_PASSWORD_KEY + username, encodeBytesToString(encryptedPassword));
        editor.apply();
    }

    private static String encodeBytesToString(byte[] bytes) {
        return android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
    }

    private static byte[] decodeStringToBytes(String str) {
        return android.util.Base64.decode(str, android.util.Base64.DEFAULT);
    }





    public static String getPassword(Context context, String username) {
        byte[] encryptedPassword = getEncryptedPasswordFromSharedPreferences(context, username);
        if (encryptedPassword != null) {
            try {
                SecretKey secretKey = getSecretKeyFromKeyStore();
                return AESUtils.decrypt(encryptedPassword, secretKey);
            } catch (Exception e) {
                e.printStackTrace();
                // Fehlerbehandlung
            }
        }
        return null;
    }

    private static byte[] getEncryptedPasswordFromSharedPreferences(Context context, String username) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String encryptedPasswordBase64 = sharedPreferences.getString(ENCRYPTED_PASSWORD_KEY + username, null);
        if (encryptedPasswordBase64 != null) {
            return Base64.decode(encryptedPasswordBase64, Base64.DEFAULT);
        }
        return null;
    }

    private static SecretKey getSecretKeyFromKeyStore() {
        try {
            // Öffnen Sie den KeyStore
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
            keyStore.load(null);

            // Überprüfen, ob der Schlüssel bereits vorhanden ist
            if (!keyStore.containsAlias(KEY_ALIAS)) {
                // Wenn der Schlüssel nicht vorhanden ist, generieren Sie einen neuen
                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE);
                keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .setRandomizedEncryptionRequired(false)
                        .build());
                keyGenerator.generateKey();
            }

            // Rückgabe des Schlüssels aus dem KeyStore
            return (SecretKey) keyStore.getKey(KEY_ALIAS, null);
        } catch (NoSuchAlgorithmException | UnrecoverableKeyException | CertificateException | KeyStoreException
                 | InvalidAlgorithmParameterException | IOException | NoSuchProviderException e) {
            e.printStackTrace();
            // Fehlerbehandlung
            return null;
        }
    }


}
