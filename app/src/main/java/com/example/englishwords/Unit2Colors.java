package com.example.englishwords;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;

public class Unit2Colors extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_unit1_numbers);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton imageButton = findViewById(R.id.imageButton14);
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Unit2Colors.this, Unit2ColorsLearn.class);
            startActivity(intent);
        });
        ImageButton imageButton2 = findViewById(R.id.imageButton15);
        imageButton2.setOnClickListener(v -> {
            Intent intent = new Intent(Unit2Colors.this, Unit2ColorsTest.class);
            startActivity(intent);
        });
        ImageButton imageButton3 = findViewById(R.id.imageButton16);
        imageButton3.setOnClickListener(v -> {
            Intent intent = new Intent(Unit2Colors.this, Unit2ColorsMatch.class);
            startActivity(intent);
        });

    }
}