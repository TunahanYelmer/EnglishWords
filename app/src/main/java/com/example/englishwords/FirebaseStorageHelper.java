package com.example.englishwords;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class FirebaseStorageHelper {
    private String unitName;
    private String imageFileName;

    public FirebaseStorageHelper(String unitName, String imageFileName) {
        this.unitName = unitName;
        this.imageFileName = imageFileName;
    }

    public CompletableFuture<File> downloadImage() {
        CompletableFuture<File> future = new CompletableFuture<>();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child(unitName + "/" + imageFileName);

        try {
            File localFile = File.createTempFile("images", "jpg");
            imageRef.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image successfully downloaded
                        future.complete(localFile);
                    })
                    .addOnFailureListener(e -> {
                        // Handle any errors
                        future.completeExceptionally(e);
                    });
        } catch (IOException e) {
            future.completeExceptionally(e);
        }

        return future;
    }
}