package com.example.englishwords;

import static com.example.englishwords.Constants.MENU_ITEM_1;
import static com.example.englishwords.Constants.MENU_ITEM_2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this) ;
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton imageButton1 = findViewById(R.id.imageButton);
        imageButton1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Unit1Numbers.class);
            startActivity(intent);
        });
        ImageButton imageButton2 = findViewById(R.id.imageButton2);
        imageButton2.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Unit2Colors.class);
            startActivity(intent);
        });
        ImageButton imageButton3 = findViewById(R.id.imageButton3);
        imageButton3.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Unit3Animals.class);
            startActivity(intent);
        });
        ImageButton imageButton4 = findViewById(R.id.imageButton4);
        imageButton4.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Unit4Food.class);
            startActivity(intent);
        });
        ImageButton imageButton5 = findViewById(R.id.imageButton5);
        imageButton5.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Unit5Fruits.class);
            startActivity(intent);
        });
        ImageButton imageButton6 = findViewById(R.id.imageButton6);
        imageButton6.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Unit6Clothing.class);
            startActivity(intent);
        });
        ImageButton imageButton7 = findViewById(R.id.imageButton7);
        imageButton7.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Unit7School.class);
            startActivity(intent);
        });
        ImageButton imageButton8 = findViewById(R.id.imageButton8);
        imageButton8.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Unit8Weather.class);
            startActivity(intent);
        });



        for (int i = 1; i <= 10; i++) {
            String fileName;
            int imageButtonID;

            switch (i) {
                case 1:
                    fileName = "unit_1_sayilar";
                    imageButtonID = R.id.imageButton;
                    break;
                case 2:
                    fileName = "unit_2_renkler";
                    imageButtonID = R.id.imageButton2;
                    break;
                case 3:
                    fileName = "unit_3_hayvanlar";
                    imageButtonID = R.id.imageButton3;
                    break;
                case 4:
                    fileName = "unit_4_yemekler";
                    imageButtonID = R.id.imageButton4;
                    break;
                case 5:
                    fileName = "unit_5_meyveler";
                    imageButtonID = R.id.imageButton5;
                    break;
                case 6:
                    fileName = "unit_6_kiyafetler";
                    imageButtonID = R.id.imageButton6;
                    break;
                case 7:
                    fileName = "unit_7_okullar";
                    imageButtonID = R.id.imageButton7;
                    break;
                case 8:
                    fileName = "unit_8_aile";
                    imageButtonID = R.id.imageButton8;
                    break;

                default:
                    fileName = "unit_1_sayilar";
                    imageButtonID = R.id.imageButton;
                    break;
            }

            int reqWidth = 300;
            int reqHeight = 300;
            Bitmap bitmap = decodeSampledBitmapFromFile(fileName, reqWidth, reqHeight);
            ImageButton imageButton = findViewById(imageButtonID);
            if (bitmap != null) {
                imageButton.setImageBitmap(bitmap);
            }
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Calculate inSampleSize to scale down the image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public Bitmap decodeSampledBitmapFromFile(String fileName, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        @SuppressLint("DiscouragedApi") int resId = getResources().getIdentifier(fileName, "drawable", getPackageName());
        BitmapFactory.decodeResource(getResources(), resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(getResources(), resId, options);
    }
}


