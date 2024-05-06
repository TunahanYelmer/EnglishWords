package com.example.englishwords;
import static java.lang.String.*;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
import java.util.Objects;
import java.util.Random;

public class Unit1NumbersTest extends AppCompatActivity {
    private TextView txtScore;
    private TextView txtTime;
    private int score = 0;
    private ImageView imageView;
    private Button option1;
    private Button option2;
    private Button option3;
    private Button option4;
    private VocabularyClass exercise = new VocabularyClass();
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
        startTest();
        startTimer();

    }

    private void setOptions() {

        // Reset button colors with a slight delay
        new Handler().postDelayed(() -> {
            String lastWord = "";

            for (Button button : new Button[]{option1, option2, option3, option4}) {
                button.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_purple)); // Set default color
            }

            if (!Objects.equals(correctWord, lastWord)) {
                correctWord = getRandomWord();
                lastWord = correctWord;
            } else {
                correctWord = getRandomWord();
                lastWord = correctWord;
            }


            String correctTranslation = exercise.getTranslation(correctWord); // Use VocabularyClass method
            int correctImageResource = exercise.getImageResource(correctWord); // Use VocabularyClass method

            // Create a list to hold incorrect words
            List<String> incorrectWords = new ArrayList<>(exercise.getWords());
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
                score+=5;
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

            // Set image
            imageView.setImageResource(correctImageResource);
        }, 500);
    }
    public void startTest() {
        // Get a random word for the test
        correctWord = getRandomWord();

        // Get the image resource for the correct word
        int correctImageResource = exercise.getImageResource(correctWord);

        // Set the image for the test
        imageView.setImageResource(correctImageResource);

        // Create a list to hold incorrect words
        List<String> incorrectWords = new ArrayList<>(exercise.getWords());
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
            score+=5;
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
        String timeText = format("Kalan Süre: %02d", seconds);
        txtTime.setText(timeText);
    }

    public String getRandomWord() {
        return exercise.getRandomWord();
    }
}
