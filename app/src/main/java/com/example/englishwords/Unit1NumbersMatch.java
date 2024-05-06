package com.example.englishwords;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Random;

public class Unit1NumbersMatch extends AppCompatActivity {
    private VocabularyClass exercise;
    private int[] mImages;
    private final int[] selectedImages = new int[5];
    private final Random rnd = new Random();
    private ImageView imageView;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private DraggableButton draggableButton;
    private DraggableButton draggableButton2;
    private DraggableButton draggableButton3;
    private DraggableButton draggableButton4;
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
        mImages = exercise.getImages();
        for (int i = 0; i < 5; i++) {
            int randomIndex = rnd.nextInt(9); // Generate random index
            while (mImages[randomIndex] == 0) {
                // If the image at this index is already selected, generate a new random index
                randomIndex = rnd.nextInt(9);
            }
            selectedImages[i] = mImages[randomIndex]; // Assign the selected image to selectedImages
            mImages[randomIndex] = 0; // Mark the image as selected in mImages
        }
        imageView = findViewById(R.id.imageView3);
        imageView2 = findViewById(R.id.imageView4);
        imageView3 = findViewById(R.id.imageView5);
        imageView4 = findViewById(R.id.imageView6);
         draggableButton = findViewById(R.id.draggableButton);
         draggableButton2 = findViewById(R.id.draggableButton2);
        draggableButton3 = findViewById(R.id.draggableButton3);
         draggableButton4 = findViewById(R.id.draggableButton4);
        imageView.setImageResource(selectedImages[0]);
        imageView2.setImageResource(selectedImages[1]);
        imageView3.setImageResource(selectedImages[2]);
        imageView4.setImageResource(selectedImages[3]);


        for (int i = 0; i < 4; i++) {
            // Generate a random button index
            int randomButtonIndex = rnd.nextInt(4);
            DraggableButton randomButton;
            String randomWord;

            // Check if the button at the random index is already selected
            while (selectedButtons.contains(getDraggableButton(randomButtonIndex))) {
                // If the button is already selected, generate a new random index
                randomButtonIndex = rnd.nextInt(4);
            }

            // Select the button at the random index
            randomButton = getDraggableButton(randomButtonIndex);

            // Add the selected button to the list of selected buttons
            selectedButtons.add(randomButton);
            int randomWordIndex = rnd.nextInt(4);
            // Get the corresponding word for the selected button's image
            randomWord = exercise.getWordFromImageResource(selectedImages[randomWordIndex]);
            randomButton.setCorrespondingText(randomWord);
            randomButton.setText(randomWord);
            // Set the corresponding word to the selected button

        }

// Helper method to get DraggableButton based on index


    }

    private DraggableButton getDraggableButton(int index) {
        switch (index) {
            case 0:
                return draggableButton;
            case 1:
                return draggableButton2;
            case 2:
                return draggableButton3;
            case 3:
                return draggableButton4;
            default:
                return draggableButton;
        }
    }
}
