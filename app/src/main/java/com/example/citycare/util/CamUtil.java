package com.example.citycare.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.citycare.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CamUtil {

    private Context context;
    private File imageFile;
    private Activity landingPage;
    private Bitmap bitmap;
    public CamUtil(Context context, Activity landingPage) {
        this.context = context;
        this.landingPage = landingPage;
    }

    public Bitmap getBitmapFromFile() {

        Bitmap photoBitmap = null;
        Log.d("catchFile", imageFile.getAbsolutePath());

        if (imageFile.exists()) {
            photoBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            if (photoBitmap == null) {
                Log.d("catchBitmap", "Bitmap is null");
            } else {
                Log.d("catchBitmap", photoBitmap.toString());
            }
        } else {
            Log.d("catchFile", "File does not exist");
        }

        bitmap=photoBitmap;
        return photoBitmap;
    }
    public void setImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFile.setReadable(true);
        imageFile.setWritable(true);
    }
    public Bitmap getBitmapFromUri(Uri uri) {

        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    public String saveImageToInternalStorage(Bitmap bitmap) {
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File directory = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE);
        File myPath = new File(directory, "profile.jpg");

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(myPath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myPath.getAbsolutePath();
    }
    public void initDialog(int galleryCode, int camCode) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_input_bild);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageButton gallery = dialog.findViewById(R.id.intentGallery);
        ImageButton cam = dialog.findViewById(R.id.intentCamera);

        gallery.setOnClickListener(v -> {
            Toast toast = new Toast(context);
            toast.setText("Galerie");
            toast.show();

            Intent fotoAuswaehlenIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            landingPage.startActivityForResult(fotoAuswaehlenIntent, galleryCode);
            dialog.dismiss();
        });

        cam.setOnClickListener(v -> {
            Toast toast = new Toast(context);
            toast.setText("Kamera");
            toast.show();
            try {
                setImageFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Intent fotoMachenIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (imageFile != null) {
                Uri photoUri = FileProvider.getUriForFile(context, "com.example.citycare.fileprovider", imageFile);
                fotoMachenIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                landingPage.startActivityForResult(fotoMachenIntent, camCode);
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    public File getImageFile() {
        return imageFile;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
