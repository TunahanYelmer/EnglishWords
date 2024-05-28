package com.example.englishwords;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FireStoreDatabase {
    private String unitName;
    private CollectionReference collectionReference;
    private Map<String, Object> data;
    private VocabularyClass vocabulary;

    public FireStoreDatabase(String unitName, Context context) {
        FirebaseApp.initializeApp(context);
        this.unitName = unitName;
        this.collectionReference = FirebaseFirestore.getInstance().collection(unitName);
    }

    public void addVocabulary(String word, String translation, String imageFileName) {
        Map<String, Object> data = new HashMap<>();
        data.put("word", word);
        data.put("translation", translation);
        data.put("imageFileName", imageFileName);

        collectionReference.add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
    }

    public CompletableFuture<VocabularyClass> getVocabulary() {
        CompletableFuture<VocabularyClass> future = new CompletableFuture<>();

        collectionReference.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    vocabulary = new VocabularyClass();
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                        vocabulary.addVocabulary(
                                queryDocumentSnapshots.getDocuments().get(i).getString("word"),
                                queryDocumentSnapshots.getDocuments().get(i).getString("translation"),
                                queryDocumentSnapshots.getDocuments().get(i).getString("imageFileName")
                        );
                    }
                    future.complete(vocabulary);
                })
                .addOnFailureListener(e -> {
                    Log.w("TAG", "Error getting documents.", e);
                    future.completeExceptionally(e);
                });

        return future;
    }
}