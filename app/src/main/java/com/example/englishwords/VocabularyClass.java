package com.example.englishwords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VocabularyClass {
    private final Map<WordData, String> vocabulary;
    private final Map<String, Integer> VocabularyImageResource;
    private int[] mImages;

    public VocabularyClass() {
        // Initialize vocabulary with sample words and their corresponding images
        vocabulary = new HashMap<>();
        VocabularyImageResource = new HashMap<>();
        // Add more vocabulary words and images as needed
    }

    public String getTranslation(String word) {
        for (Map.Entry<WordData, String> entry : vocabulary.entrySet()) {
            if (entry.getKey().getWord().equals(word)) {
                return entry.getKey().getWordTranslation();
            }
        }
        return null; // Word not found
    }

    public ArrayList<String> getWords() {
        ArrayList<String> words = new ArrayList<>();
        for (Map.Entry<WordData, String> entry : vocabulary.entrySet()) {
            words.add(entry.getKey().getWord());
        }
        return words;
    }

    public void addVocabulary(String word, String wordTranslation, String imageFileName) {
        // Add a new word and its corresponding translation and image file name to the vocabulary
        vocabulary.put(new WordData(word, wordTranslation), imageFileName);
    }

    public String getRandomWord() {
        // Get a random word from the vocabulary
        int index = (int) (Math.random() * vocabulary.size());
        return vocabulary.keySet().toArray(new WordData[0])[index].getWord();
    }

    public String getImageFileName(String word) {
        // Get the image file name corresponding to the given word
        for (Map.Entry<WordData, String> entry : vocabulary.entrySet()) {
            if (entry.getKey().getWord().equals(word)) {
                return entry.getValue();
            }
        }
        return null; // Word not found
    }
    public String getWordFromImageResource(int imageResourceID) {
        // Get the word corresponding to the given image resource ID
        for (Map.Entry<WordData, String> entry : vocabulary.entrySet()) {
            try {
                if (VocabularyImageResource.get(entry.getKey().getWord()) == imageResourceID) {
                    System.out.println(entry.getKey().getWord());
                    return entry.getKey().getWord();
                }
            } catch (Exception e) {
                System.out.println("Error: " + e);
                // You might want to handle the exception appropriately here
                return null; // Return null if there's an error
            }
        }
        return null; // Return null if no matching word is found
    }

    public void addVocabularyImageResource(String word, int imageResourceID) {
        VocabularyImageResource.put(word, imageResourceID);

    }

    public int getImageResource(String word) {
        // Get the image resource ID corresponding to the given word
        for (Map.Entry<String, Integer> entry : VocabularyImageResource.entrySet()) {
            if (entry.getKey().equals(word)) {
                return entry.getValue();
            }
        }
        return 0; // Word not found
    }

    public int[] getImages() {
        // Get the image resource ID corresponding to the given word
        mImages = new int[VocabularyImageResource.size()];
        int i = 0;
        for (Map.Entry<String, Integer> entry : VocabularyImageResource.entrySet()) {
            mImages[i] = entry.getValue();
            i++;
        }
        return mImages;
    }

    public int size() {
        return vocabulary.size();
    }

    // Inner class to encapsulate word and translation
    public static class WordData {
        private final String word;
        private final String wordTranslation;

        public WordData(String word, String wordTranslation) {
            this.word = word;
            this.wordTranslation = wordTranslation;
        }

        public String getWord() {
            return word;
        }

        public String getWordTranslation() {
            return wordTranslation;
        }
    }
}