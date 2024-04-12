package com.example.citycare.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    public Bitmap getBitmap(File photoFile) {

        Bitmap photoBitmap = null;
        Log.d("catchFile", photoFile.getAbsolutePath());

        if (photoFile.exists()) {
            photoBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            if (photoBitmap == null) {
                Log.d("catchBitmap", "Bitmap is null");
            } else {
                Log.d("catchBitmap", photoBitmap.toString());
            }
        } else {
            Log.d("catchFile", "File does not exist");
        }


        return photoBitmap;
    }
    public File getImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }
    public Bitmap getBitmapFromUri(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
