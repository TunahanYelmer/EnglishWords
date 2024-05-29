package com.example.englishwords;
import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class Unit3AnimalsLearn extends AppCompatActivity {
    private ImageView imageView;
    private TextView wordTextView;
    private TextView translationTextView;
    private Button speakButtonEnglish;
    private Button speakButtonTurkish;
    private String word;
    private String wordTranslation;
    private FirebaseStorageHelper firebaseStorageHelper;


    private VocabularyClass exercise; // Changed from VocabularyClass to FirebaseDatabaseOperations
    private TextToSpeechAgent speaker;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_unit1_numbers_learn);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imageView = findViewById(R.id.imageView);
        wordTextView = findViewById(R.id.textView);
        translationTextView = findViewById(R.id.textView2);
        speakButtonEnglish = findViewById(R.id.button3);
        speakButtonTurkish = findViewById(R.id.button2);


        // Initialize Firebase operations
        displayNextQuestion();
        speaker = new TextToSpeechAgent(this, Locale.ENGLISH);
        nextButton = findViewById(R.id.button);
        nextButton.setOnClickListener(v -> {
            displayNextQuestion();
        });
    }

    private void displayNextQuestion() {
        FireStoreDatabase fireStoreDatabase = new FireStoreDatabase("Animals", Unit3AnimalsLearn.this);
        fireStoreDatabase.getVocabulary().thenAccept(vocabulary -> {
            // This block will be executed when the vocabulary is loaded
            if (vocabulary != null && !vocabulary.getWords().isEmpty()) {
                // Get a random word from the vocabulary
                String word = vocabulary.getRandomWord();
                String wordTranslation = vocabulary.getTranslation(word);
                this.word = word;
                this.wordTranslation = wordTranslation;
                FirebaseStorageHelper firebaseStorageHelper = new FirebaseStorageHelper("Animals",vocabulary.getImageFileName(word));
                firebaseStorageHelper.downloadImage().thenAccept(file -> {
                    Log.d(TAG, "Image loaded : " +  vocabulary.getImageFileName(word));
                    // This block will be executed when the image is downloaded
                    if (file != null) {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        // Run on UI thread since UI operations can't be done on a background thread
                        runOnUiThread(() -> imageView.setImageBitmap(bitmap));
                    }
                }).exceptionally(e -> {
                    // This block will be executed if there was an error downloading the image
                    Log.e(TAG, "Error loading image: " + e.getMessage());
                    return null;
                });


                // Set the word to the TextView
                wordTextView.setText(word);
                translationTextView.setText(wordTranslation);

                // Set up the speak buttons
                speakButtonEnglish.setOnClickListener(v -> {
                    speaker.setLanguage(Locale.ENGLISH);
                    speaker.speak(word);
                });
                speakButtonTurkish.setOnClickListener(v -> {
                    speaker.setLanguage(new Locale("tr", "TR"));
                    speaker.speak(wordTranslation);
                });

                Log.d(TAG, "displayNextQuestion: " + word);
            }
        }).exceptionally(e -> {
            // This block will be executed if there was an error loading the vocabulary
            System.out.println("Error loading vocabulary: " + e.getMessage());
            return null;
        });
    }
}