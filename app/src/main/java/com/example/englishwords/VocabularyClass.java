package com.example.englishwords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VocabularyClass {
    private final Map<WordData, String> vocabulary;
    private final Map<String, Integer> vocabularyImageResource;

    public VocabularyClass() {
        vocabulary = new HashMap<>();
        vocabularyImageResource = new HashMap<>();
    }

    public String getTranslation(String word) {
        for (Map.Entry<WordData, String> entry : vocabulary.entrySet()) {
            if (entry.getKey().getWord().equals(word)) {
                return entry.getKey().getWordTranslation();
            }
        }
        return null;
    }

    public ArrayList<String> getWords() {
        ArrayList<String> words = new ArrayList<>();
        for (Map.Entry<WordData, String> entry : vocabulary.entrySet()) {
            words.add(entry.getKey().getWord());
        }
        return words;
    }

    public void addVocabulary(String word, String wordTranslation, String imageFileName) {
        if (word == null || word.isEmpty()) {
            throw new IllegalArgumentException("word cannot be null or empty");
        }
        if (wordTranslation == null || wordTranslation.isEmpty()) {
            throw new IllegalArgumentException("wordTranslation cannot be null or empty");
        }
        if (imageFileName == null || imageFileName.isEmpty()) {
            throw new IllegalArgumentException("imageFileName cannot be null or empty");
        }
        vocabulary.put(new WordData(word, wordTranslation), imageFileName);
    }

    public String getRandomWord() {
        if (vocabulary.isEmpty()) {
            return null;
        }
        int index = (int) (Math.random() * vocabulary.size());
        return vocabulary.keySet().toArray(new WordData[0])[index].getWord();
    }

    public String getImageFileName(String word) {
        for (Map.Entry<WordData, String> entry : vocabulary.entrySet()) {
            if (entry.getKey().getWord().equals(word)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void addVocabularyImageResource(String word, int imageResourceID) {
        vocabularyImageResource.put(word, imageResourceID);
    }

    public int getImageResource(String word) {
        Integer resourceID = vocabularyImageResource.get(word);
        return resourceID != null ? resourceID : 0;
    }

    public int[] getImages() {
        int[] images = new int[vocabularyImageResource.size()];
        int i = 0;
        for (int imageResourceID : vocabularyImageResource.values()) {
            images[i] = imageResourceID;
            i++;
        }
        return images;
    }

    public String getWordFromImageResource(int imageResourceID) {
        for (Map.Entry<String, Integer> entry : vocabularyImageResource.entrySet()) {
            if (entry.getValue() == imageResourceID) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Map<String, String> getWordToImageFileName() {
        Map<String, String> wordToImageFileName = new HashMap<>();
        for (Map.Entry<WordData, String> entry : vocabulary.entrySet()) {
            wordToImageFileName.put(entry.getKey().getWord(), entry.getValue());
        }
        return wordToImageFileName;
    }

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
