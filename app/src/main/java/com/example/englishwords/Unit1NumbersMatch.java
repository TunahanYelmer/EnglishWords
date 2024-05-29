package com.example.englishwords;

import static android.content.ContentValues.TAG;
import static android.view.DragEvent.ACTION_DROP;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
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
    private TextView scoreTextView;
    private CountDownTimer countDownTimer;

    private TextView timeTextView;
    private long timeLeftInMillis = 60000; // Initial time in milliseconds
    private final ArrayList<DraggableButton> selectedButtons = new ArrayList<>();
    private int score;
    private ImageAdapter imageAdapter;

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

        scoreTextView = findViewById(R.id.txtScore);
        timeTextView = findViewById(R.id.txtTime);
        score = 0;
        scoreTextView.setText("Score: " + score);

        startTimer(timeLeftInMillis); // Start the timer

        VocabularyClass vocabulary = new VocabularyClass();
        FireStoreDatabase fireStoreDatabase = new FireStoreDatabase("Numbers", Unit1NumbersMatch.this);
        CompletableFuture<VocabularyClass> vocabularyFuture = fireStoreDatabase.getVocabulary();

        vocabularyFuture.thenAccept(vocab -> {
            if (vocab != null && !vocab.getWords().isEmpty()) {
                exercise = vocab;
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

    private void startTimer(long durationInMillis) {
        countDownTimer = new CountDownTimer(durationInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                timeTextView.setText("Time: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timeTextView.setText("Time's up!");
            }
        }.start();
    }

    private void initializeImages() {
        selectedPairs.clear();  // Clear the old pairs

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

        View.OnDragListener dragListener = new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                Log.d(TAG, "onDrag: ");
                onDraggableButtonDrop(v, event);
                return true;
            }
        };

        imageViewTop.setOnDragListener(dragListener);
        imageViewMiddleTop.setOnDragListener(dragListener);
        imageViewMiddleBottom.setOnDragListener(dragListener);
        imageViewBottom.setOnDragListener(dragListener);
    }

    private void loadImageIntoView(ImageView imageView, String imageFileName) {
        FirebaseStorageHelper firebaseStorageHelper = new FirebaseStorageHelper("Numbers", imageFileName);
        firebaseStorageHelper.downloadImage().thenAccept(file -> {
            if (file != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                runOnUiThread(() -> {
                    imageView.setImageBitmap(bitmap);
                    imageView.setTag(imageFileName);  // Set the image name to the ImageView's tag
                });
            }
        }).exceptionally(e -> {
            Log.e(TAG, "Error loading image: " + e.getMessage());
            return null;
        });
    }

    private void initializeDraggableButtons() {
        selectedButtons.clear();

        draggableButtonTopLeft = findViewById(R.id.draggableButtonTopLeft);
        draggableButtonTopRight = findViewById(R.id.draggableButtonTopRight);
        draggableButtonBottomLeft = findViewById(R.id.draggableButtonBottomLeft);
        draggableButtonBottomRight = findViewById(R.id.draggableButtonBottomRight);

        DraggableButton[] buttons = {draggableButtonTopLeft, draggableButtonTopRight, draggableButtonBottomLeft, draggableButtonBottomRight};

        // Shuffle the buttons
        shuffleArray(buttons);

        for (int i = 0; i < 4; i++) {
            DraggableButton randomButton = buttons[i];
            randomButton.setCorrect(false);
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

    private boolean allButtonsCorrect() {
        for (DraggableButton button : selectedButtons) {
            if (!button.isCorrect()) {
                return false;
            }
        }
        return true;
    }

    public void onDraggableButtonDrop(View view, DragEvent event) {
        if (event.getAction() == ACTION_DROP) {
            // Get the DraggableButton that was dragged
            DraggableButton button = (DraggableButton) event.getLocalState();
            String correspondingText = button.getCorrespondingText();
            if (correspondingText != null) {
                // Get the ImageView that the button was dropped on
                ImageView droppedImageView = (ImageView) view;
                // Get the image name from the ImageView's tag
                String imageName = (String) droppedImageView.getTag();
                Log.d(TAG, "Image name: " + imageName);
                // Get the word corresponding to the image
                String imageViewWord = exercise.getWordFromImageName(imageName);
                Log.d(TAG, "onDraggableButtonDrop: " + imageViewWord);

                for (DraggableButton selectedButton : selectedButtons) {
                    if (selectedButton.equals(button)) {
                        if (imageViewWord != null && imageViewWord.equals(selectedButton.getCorrespondingText())) {
                            selectedButton.setCorrect(true);
                            score++;  // Increment the score
                            scoreTextView.setText("Score: " + score);  // Update the score text view

                            // Add time to the timer
                            timeLeftInMillis += 5000;  // Add 5 seconds
                            countDownTimer.cancel();
                            startTimer(timeLeftInMillis);  // Restart the timer with the updated time

                            // Break out of the loop since we found the matching button
                            break;
                        }
                    }
                }

                if (allButtonsCorrect()) {
                    initializeImages();
                    initializeDraggableButtons();
                }
            }
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
