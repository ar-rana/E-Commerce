package com.practice.ecommerce.config;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseConfig {

    @Bean
    FirebaseApp initializeFirebase() throws IOException {
        // avoid 'FirebaseApp customer [DEFAULT] already exists' Exception
        if (!FirebaseApp.getApps().isEmpty()) return FirebaseApp.getInstance();
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/static/serviceAccountKey.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        return FirebaseApp.initializeApp(options);
    }

    // if we dont put the 'FirebaseApp firebaseApp' in parameter then these Beans get called early
    // and throw the UnsatisfiedDependencyException FirebaseApp with customer [DEFAULT] doesn't exist.
    // so to make sure they get called after the FirebaseApp init we set it as parameter
    @Bean
    Firestore getFirestore(FirebaseApp firebaseApp) {
        Firestore db = FirestoreClient.getFirestore();
        return db;
    }
}
