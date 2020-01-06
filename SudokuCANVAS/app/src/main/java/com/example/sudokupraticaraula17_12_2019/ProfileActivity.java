package com.example.sudokupraticaraula17_12_2019;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Method;

public class ProfileActivity extends AppCompatActivity {

    String imageFilePath = "/sdcard/tmpfoto.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onAbrirCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri fileUri = Uri.fromFile(new File(imageFilePath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, 20);
    }

    public void onAbrirGaleria(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri _uri = data.getData();
            if (_uri != null) {
                Cursor cursor = getContentResolver().query(_uri,
                        new String[]{MediaStore.Images.ImageColumns.DATA},
                        null, null, null);
                cursor.moveToFirst();
                imageFilePath = cursor.getString(0);
                cursor.close();
            }
        }

        setPic((ImageView) findViewById(R.id.ivProfilePic), imageFilePath);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        Bundle extras = getIntent().getExtras();

        if (extras != null){
            String name = extras.getString("name");
            if (name != null) {
                ((EditText) findViewById(R.id.etNome)).setText(name);
            }

            String image = extras.getString("img");
            if (image != null) {
                imageFilePath = image;
                setPic((ImageView) findViewById(R.id.ivProfilePic), imageFilePath);
            }
        }
    }

    public void onSave(View v) {
        String nickname = ((EditText) findViewById(R.id.etNome)).getText().toString();
        /*if (nickname.isEmpty()) {
            Toast.makeText(this, "O Nome é Obritatório", Toast.LENGTH_SHORT).show();
            findViewById(R.id.nometv).requestFocus();
            return;
        }
        if (imageFilePath.equals("/sdcard/temp.png")) {
            Toast.makeText(this, "Necessita fazer upload de uma imagem", Toast.LENGTH_SHORT).show();
            findViewById(R.id.take_picture).requestFocus();
            return;
        }*/
        Intent intent = new Intent();
        intent.putExtra("name", nickname);
        intent.putExtra("img", imageFilePath);
        setResult(RESULT_OK, intent);
        finish();
    }


    private void setPic(ImageView mImageView, String mCurrentPhotoPath) {
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();
        if (targetW == 0 || targetH == 0)
            return;

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }
}
