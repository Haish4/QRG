package com.example.qrg;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.qrg.databinding.ActivityQrgeneratorBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class QRGeneratorActivity extends AppCompatActivity {

    private ActivityQrgeneratorBinding binding;

    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQrgeneratorBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        setListener();
    }

    private void setListener() {
        binding.generate.setOnClickListener(v -> generate());
        binding.download.setOnClickListener(v -> download());
        binding.github.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://github.com/Haish4");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        binding.instagram.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://www.instagram.com/_.haisha/profilecard/?igsh=bDVpZGtiOG04YTZs");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }

    private void generate() {

        if (binding.inputText.getText().toString().isEmpty()){
            showToast("Please input your text or link first");
            return;
        }
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(binding.inputText.getText().toString().trim(), BarcodeFormat.QR_CODE, 150, 150);

                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                image = bitmap;
                loadQr(image);
            } catch (WriterException e) {
                showToast("Cant generate");
                throw new RuntimeException(e);
            }


    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void loadQr(Bitmap bitmap){
        binding.QRiv.setImageBitmap(bitmap);
    }

    private void download() {
        if (image == null) {
            showToast("Please generate the QR first");
            return;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytesArray = byteArrayOutputStream.toByteArray();

        ContentResolver contentResolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis() + ".jpeg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/QRZ");

        Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        if (imageUri != null) {
            try (OutputStream outputStream = contentResolver.openOutputStream(imageUri)) {
                if (outputStream != null) {
                    outputStream.write(bytesArray);
                    showToast("Downloaded");
                }
            } catch (IOException e) {
                showToast("Error Downloading");
                e.printStackTrace();
            }
        } else {
            showToast("Error creating file");
        }
    }



}
