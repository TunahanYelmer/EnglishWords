package com.example.englishwords;
import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class TextToSpeechAgent implements TextToSpeech.OnInitListener {
    private TextToSpeech textToSpeech;
    private boolean isInitialized = false;

    public TextToSpeechAgent(Context context, Locale locale) {
        textToSpeech = new TextToSpeech(context, this);

    }

    public void speak(String text) {
        if (isInitialized) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true;
        }
    }

    public void shutdown() {
        textToSpeech.shutdown();
    }

    public void setLanguage(Locale locale) {
        if (textToSpeech != null) {
            int result = textToSpeech.setLanguage(locale);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                isInitialized = false;
            } else {
                isInitialized = true;
            }
        }
    }
}
