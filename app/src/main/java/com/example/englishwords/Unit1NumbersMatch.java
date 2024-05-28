package com.example.englishwords;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class Unit1NumbersMatch extends AppCompatActivity {
    private VocabularyClass exercise;
    private final List<Pair> selectedPairs = new ArrayList<>();
    private final Random rnd = new Random();
    private ImageView imageViewTop;
    private ImageView imageViewMiddleTop;
    private ImageView imageViewMiddleBottom;
    private ImageView imageViewBottom;
    private DraggableButton draggableButtonTopLeft;
    private DraggableButton draggableButtonTopRight;
    private DraggableButton draggableButtonBottomLeft;
    private DraggableButton draggableButtonBottomRight;
    private final ArrayList<DraggableButton> selectedButtons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_unit1_numbers_match);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FireStoreDatabase fireStoreDatabase = new FireStoreDatabase("Numbers", Unit1NumbersMatch.this);
        CompletableFuture<VocabularyClass> vocabularyFuture = fireStoreDatabase.getVocabulary();

        vocabularyFuture.thenAccept(vocabulary -> {
            if (vocabulary != null && !vocabulary.getWords().isEmpty()) {
                exercise = vocabulary;
                runOnUiThread(() -> {
                    initializeImages();
                    initializeDraggableButtons();
                });
            }
        }).exceptionally(e -> {
            Log.e(TAG, "Error loading vocabulary: " + e.getMessage());
            return null;
        });
    }

    private void initializeImages() {
        ArrayList<String> wordsList = exercise.getWords();
        Map<String, String> wordToImageFileName = exercise.getWordToImageFileName();

        List<String> selectedWords = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int randomIndex = rnd.nextInt(wordsList.size());
            while (selectedWords.contains(wordsList.get(randomIndex))) {
                randomIndex = rnd.nextInt(wordsList.size());
            }
            String selectedWord = wordsList.get(randomIndex);
            selectedWords.add(selectedWord);
            selectedPairs.add(new Pair(selectedWord, wordToImageFileName.get(selectedWord)));
        }

        imageViewTop = findViewById(R.id.imageViewTop);
        imageViewMiddleTop = findViewById(R.id.imageViewMiddleTop);
        imageViewMiddleBottom = findViewById(R.id.imageViewMiddleBottom);
        imageViewBottom = findViewById(R.id.imageViewBottom);

        loadImageIntoView(imageViewTop, selectedPairs.get(0).getImageFileName());
        loadImageIntoView(imageViewMiddleTop, selectedPairs.get(1).getImageFileName());
        loadImageIntoView(imageViewMiddleBottom, selectedPairs.get(2).getImageFileName());
        loadImageIntoView(imageViewBottom, selectedPairs.get(3).getImageFileName());
    }

    private void loadImageIntoView(ImageView imageView, String imageFileName) {
        FirebaseStorageHelper firebaseStorageHelper = new FirebaseStorageHelper("Numbers", imageFileName);
        firebaseStorageHelper.downloadImage().thenAccept(file -> {
            if (file != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                runOnUiThread(() -> imageView.setImageBitmap(bitmap));
            }
        }).exceptionally(e -> {
            Log.e(TAG, "Error loading image: " + e.getMessage());
            return null;
        });
    }

    private void initializeDraggableButtons() {
        draggableButtonTopLeft = findViewById(R.id.draggableButtonTopLeft);
        draggableButtonTopRight = findViewById(R.id.draggableButtonTopRight);
        draggableButtonBottomLeft = findViewById(R.id.draggableButtonBottomLeft);
        draggableButtonBottomRight = findViewById(R.id.draggableButtonBottomRight);

        DraggableButton[] buttons = { draggableButtonTopLeft, draggableButtonTopRight, draggableButtonBottomLeft, draggableButtonBottomRight };

        // Shuffle the buttons
        shuffleArray(buttons);

        for (int i = 0; i < 4; i++) {
            DraggableButton randomButton = buttons[i];
            String randomWord = selectedPairs.get(i).getWord();
            randomButton.setCorrespondingText(randomWord);
            randomButton.setText(randomWord);
            selectedButtons.add(randomButton);
        }
    }

    // Fisher-Yates shuffle algorithm
    private void shuffleArray(DraggableButton[] buttons) {
        Random rnd = new Random();
        for (int i = buttons.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Swap
            DraggableButton temp = buttons[index];
            buttons[index] = buttons[i];
            buttons[i] = temp;
        }
    }

    private DraggableButton getDraggableButton(int index) {
        switch (index) {
            case 0:
                return draggableButtonTopLeft;
            case 1:
                return draggableButtonTopRight;
            case 2:
                return draggableButtonBottomLeft;
            case 3:
                return draggableButtonBottomRight;
            default:
                return draggableButtonTopLeft;
        }
    }

    private static class Pair {
        private final String word;
        private final String imageFileName;

        Pair(String word, String imageFileName) {
            this.word = word;
            this.imageFileName = imageFileName;
        }

        public String getWord() {
            return word;
        }

        public String getImageFileName() {
            return imageFileName;
        }
    }
}
