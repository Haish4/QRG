package com.example.qrg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
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
    }

    private void generate() {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(binding.inputText.getText().toString().trim(), BarcodeFormat.QR_CODE, 150,150);

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
        StorageManager storageManager = (StorageManager) getSystemService(STORAGE_SERVICE);
        StorageVolume storageVolume = storageManager.getStorageVolumes().get(0);
        File fileImage = new File(storageVolume.getDirectory().getPath() + "/Download/" + System.currentTimeMillis() + ".jpeg");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (image != null){
            image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] bytesArray = byteArrayOutputStream.toByteArray();
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(fileImage);
                    fileOutputStream.write(bytesArray);
                    fileOutputStream.close();
                    showToast("Downloaded");
                }catch (IOException e){
                    showToast("Error Downloading");
                    throw new RuntimeException();
                }
        }else {
            showToast("Please generate the qr first");
        }

    }

}
