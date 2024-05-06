package com.example.englishwords;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.lang.reflect.Field;
import java.util.Locale;

public class Unit1NumbersLearn extends AppCompatActivity {
    private ImageView imageView;
    private TextView wordTextView;
    private TextView translationTextView;
    private Button speakButtonEnglish;
    private Button speakButtonTurkish;
    private VocabularyClass exercise;
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

        exercise = new VocabularyClass();
        exercise.addVocabulary("Zero", "Sıfır", "number_0.jpg");
        exercise.addVocabulary("One", "Bir", "number_1.jpg");
        exercise.addVocabulary("Two", "İki", "number_2.jpg");
        exercise.addVocabulary("Three", "Üç", "number_3.jpg");
        exercise.addVocabulary("Four", "Dört", "number_4.jpg");
        exercise.addVocabulary("Five", "Beş", "number_5.jpg");
        exercise.addVocabulary("Six", "Altı", "number_6.jpg");
        exercise.addVocabulary("Seven", "Yedi", "number_7.jpg");
        exercise.addVocabulary("Eight", "Sekiz", "number_8.jpg");
        exercise.addVocabulary("Nine", "Dokuz", "number_9.jpg");
        exercise.addVocabularyImageResource("Zero", R.drawable.number_0);
        exercise.addVocabularyImageResource("One", R.drawable.number_1);
        exercise.addVocabularyImageResource("Two", R.drawable.number_2);
        exercise.addVocabularyImageResource("Three", R.drawable.number_3);
        exercise.addVocabularyImageResource("Four", R.drawable.number_4);
        exercise.addVocabularyImageResource("Five", R.drawable.number_5);
        exercise.addVocabularyImageResource("Six", R.drawable.number_6);
        exercise.addVocabularyImageResource("Seven", R.drawable.number_7);
        exercise.addVocabularyImageResource("Eight", R.drawable.number_8);
        exercise.addVocabularyImageResource("Nine", R.drawable.number_9);
        speaker = new TextToSpeechAgent(this, Locale.ENGLISH);
        int numberOfVocabularyWords = exercise.size();

            displayNextQuestion();
            nextButton = findViewById(R.id.button);
            nextButton.setOnClickListener(v -> {
                displayNextQuestion();
            });
    }
    private void displayNextQuestion() {
        // Get a random word from the exercise
        String word = exercise.getRandomWord();
        String wordTranslation = exercise.getTranslation(word);
        // Load and display corresponding image
        // Load and display corresponding image
        String imageName = exercise.getImageFileName(word);
        int resId = exercise.getImageResource(word);
        if (resId != 0) { // Check if the resource was found

            imageView.setImageResource(resId);
        } else {
            // Display a toast message if the image resource was not found
            Toast.makeText(this, "Image resource not found for " + resId, Toast.LENGTH_SHORT).show();
        }
        // Display the word
        wordTextView.setText(word);
        translationTextView.setText(wordTranslation);
        speakButtonEnglish.setOnClickListener(v -> {
            speaker.setLanguage(Locale.ENGLISH);
            speaker.speak(word);
        });
        speakButtonTurkish.setOnClickListener(v -> {
            speaker.setLanguage(new Locale("tr", "TR"));
            speaker.speak(wordTranslation);
        });

    }
}