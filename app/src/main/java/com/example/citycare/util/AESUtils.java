package com.example.citycare.util;
import java.io.ByteArrayOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class AESUtils {

    public static byte[] encrypt(String password, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Generieren Sie einen Initialisierungsvektor (IV)
        byte[] iv = cipher.getIV();

        // Verschlüsseln Sie das Passwort
        byte[] encryptedPassword = cipher.doFinal(password.getBytes("UTF-8"));

        // Fügen Sie den IV und das verschlüsselte Passwort zusammen
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(iv);
        outputStream.write(encryptedPassword);
        return outputStream.toByteArray();
    }

    public static String decrypt(byte[] encryptedData, SecretKey secretKey) throws Exception {
        // Trennen Sie den Initialisierungsvektor (IV) und das verschlüsselte Passwort
        byte[] iv = new byte[16];
        System.arraycopy(encryptedData, 0, iv, 0, iv.length);
        byte[] encryptedPassword = new byte[encryptedData.length - iv.length];
        System.arraycopy(encryptedData, iv.length, encryptedPassword, 0, encryptedPassword.length);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

        // Entschlüsseln Sie das Passwort
        byte[] decryptedPassword = cipher.doFinal(encryptedPassword);

        return new String(decryptedPassword, "UTF-8");
    }

}
