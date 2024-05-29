package com.example.englishwords;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class Unit2ColorsTest extends AppCompatActivity {
    private TextView txtScore;
    private TextView txtTime;
    private int score = 0;
    private ImageView imageView;
    private Button option1;
    private Button option2;
    private Button option3;
    private Button option4;
    private String correctWord;
    private Random random = new Random();
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 60000; // 60 seconds
    private final long COUNTDOWN_INTERVAL = 1000; // 1 second

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_unit1_numbers_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imageView = findViewById(R.id.imageView2);
        option1 = findViewById(R.id.button7);
        option2 = findViewById(R.id.button6);
        option3 = findViewById(R.id.button5);
        option4 = findViewById(R.id.button4);
        txtTime = findViewById(R.id.txtTime);
        txtScore = findViewById(R.id.txtScore);
        startTest();
        startTimer();
    }

    private void setOptions() {
        // Reset button colors with a slight delay
        new Handler().postDelayed(() -> {
            FireStoreDatabase fireStoreDatabase = new FireStoreDatabase("Colors", Unit2ColorsTest.this);
            fireStoreDatabase.getVocabulary().thenAccept(vocabulary -> {
                // This block will be executed when the vocabulary is loaded
                if (vocabulary != null && !vocabulary.getWords().isEmpty()) {
                    // Get a random word from the vocabulary
                    correctWord = vocabulary.getRandomWord();

                    FirebaseStorageHelper firebaseStorageHelper = new FirebaseStorageHelper("Colors", vocabulary.getImageFileName(correctWord));
                    firebaseStorageHelper.downloadImage().thenAccept(file -> {
                        Log.d(TAG, "Image loaded : " + vocabulary.getImageFileName(correctWord));
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

                    // Create a list to hold incorrect words
                    List<String> incorrectWords = new ArrayList<>(vocabulary.getWords());
                    incorrectWords.remove(correctWord); // Remove the correct word

                    // Shuffle the list of incorrect words
                    Collections.shuffle(incorrectWords);

                    // Assign correct and incorrect options to buttons
                    List<Button> buttons = new ArrayList<>();
                    buttons.add(option1);
                    buttons.add(option2);
                    buttons.add(option3);
                    buttons.add(option4);

                    // Assign correct translation to a random button
                    int correctButtonIndex = random.nextInt(buttons.size());
                    buttons.get(correctButtonIndex).setText(correctWord);
                    buttons.get(correctButtonIndex).setOnClickListener(v -> {
                        addTime(10000);
                        buttons.get(correctButtonIndex).setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_light));
                        score += 5;
                        txtScore.setText("Puan: " + score);
                        setOptions();
                    });

                    // Assign incorrect translations to other buttons
                    for (int i = 0; i < buttons.size(); i++) {
                        if (i != correctButtonIndex) {
                            if (!incorrectWords.isEmpty()) {
                                String incorrectWord = incorrectWords.get(0);
                                final int finalI = i;
                                buttons.get(i).setText(incorrectWord); // Use VocabularyClass method
                                buttons.get(i).setOnClickListener(v -> {
                                    buttons.get(finalI).setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
                                    setOptions();
                                });
                                incorrectWords.remove(0);
                            } else {
                                buttons.get(i).setText("Placeholder");
                                // Handle click for placeholder button
                            }
                        }
                    }
                }
            }).exceptionally(e -> {
                // This block will be executed if there was an error loading the vocabulary
                Log.e(TAG, "Error loading vocabulary: " + e.getMessage());
                return null;
            });
        }, 500);
    }

    public void startTest() {
        FireStoreDatabase fireStoreDatabase = new FireStoreDatabase("Colors", Unit2ColorsTest.this);
        fireStoreDatabase.getVocabulary().thenAccept(vocabulary -> {
            // This block will be executed when the vocabulary is loaded
            if (vocabulary != null && !vocabulary.getWords().isEmpty()) {
                // Get a random word for the test
                correctWord = vocabulary.getRandomWord();

                FirebaseStorageHelper firebaseStorageHelper = new FirebaseStorageHelper("Colors", vocabulary.getImageFileName(correctWord));
                firebaseStorageHelper.downloadImage().thenAccept(file -> {
                    Log.d(TAG, "Image loaded : " + vocabulary.getImageFileName(correctWord));
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

                // Create a list to hold incorrect words
                List<String> incorrectWords = new ArrayList<>(vocabulary.getWords());
                incorrectWords.remove(correctWord); // Remove the correct word

                // Shuffle the list of incorrect words
                Collections.shuffle(incorrectWords);

                // Assign correct and incorrect options to buttons
                List<Button> buttons = new ArrayList<>();
                buttons.add(option1);
                buttons.add(option2);
                buttons.add(option3);
                buttons.add(option4);

                // Assign correct word to a random button
                int correctButtonIndex = random.nextInt(buttons.size());
                buttons.get(correctButtonIndex).setText(correctWord);
                buttons.get(correctButtonIndex).setOnClickListener(v -> {
                    addTime(10000);
                    buttons.get(correctButtonIndex).setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_light));
                    score += 5;
                    txtScore.setText("Puan: " + score);
                    setOptions();
                });
                for (int i = 0; i < buttons.size(); i++) {
                    if (i != correctButtonIndex) {
                        if (!incorrectWords.isEmpty()) {
                            String incorrectWord = incorrectWords.get(0);
                            final int finalI = i;
                            buttons.get(i).setText(incorrectWord); // Use VocabularyClass method
                            buttons.get(i).setOnClickListener(v -> {
                                buttons.get(finalI).setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
                                setOptions();
                            });
                            incorrectWords.remove(0);
                        } else {
                            buttons.get(i).setText("Placeholder");
                            // Handle click for placeholder button
                        }
                    }
                }
            }
        }).exceptionally(e -> {
            // This block will be executed if there was an error loading the vocabulary
            Log.e(TAG, "Error loading vocabulary: " + e.getMessage());
            return null;
        });
    }

    private void addTime(long millisToAdd) {
        countDownTimer.cancel();
        timeLeftInMillis += millisToAdd;
        startTimer();
        updateCountDownText();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                // Timer finished, handle accordingly
            }
        }.start();
    }

    public void updateCountDownText() {
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeText = String.format("Kalan Süre: %02d", seconds);
        txtTime.setText(timeText);
    }
}