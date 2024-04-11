package com.example.citycare.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CamUtil {

    private Context context;
    public CamUtil(Context context) {
        this.context = context;
    }

    public Uri getUri(){
        File photoFile;
        Uri photoUri;
        try {

            photoFile = getImageFile();
            photoFile.setReadable(true);
            photoFile.setWritable(true);
            photoUri = FileProvider.getUriForFile(context, "com.example.citycare.fileprovider", photoFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return photoUri;
    }
    public File getImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }
    public static void copyImage(Context context, Uri sourceUri, Uri destinationUri) {
        try {
            // Öffne InputStream für die Quelldatei
            InputStream inputStream = context.getContentResolver().openInputStream(sourceUri);
            // Öffne OutputStream für die Zieldatei
            OutputStream outputStream = context.getContentResolver().openOutputStream(destinationUri);
            // Kopiere die Daten von InputStream zu OutputStream
            copyStream(inputStream, outputStream);

            // Schließe die Streams
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }
}
