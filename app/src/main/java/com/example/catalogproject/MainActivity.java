package com.example.catalogproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.catalogproject.Logic.Book;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.sync.DefaultSyncConflictResolvers;

import java.util.ArrayList;

/**
 * Home Activity
 * Launched when app first opened, has buttons to search and add
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The collection that we use to store books
     */
    private static RemoteMongoCollection mongoCollection;

    /**
     * Used in obtaining mongoCollection
     */
    private static RemoteMongoClient mongoClient;

    /**
     * Called when activity is started
     * @param savedInstanceState unused
     */
    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // MongoDB setup stuff
        if (mongoClient == null) {
            final StitchAppClient client = Stitch.initializeAppClient("catalogproject125-ylent");
            client.getAuth().loginWithCredential(new AnonymousCredential())
                    .addOnCompleteListener(task -> {
                        Log.d("Bookie", "assigning mongoClient");
                        mongoClient = client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
                        mongoCollection = mongoClient.getDatabase("All").getCollection("Books");
                        mongoCollection.sync().configure(
                                DefaultSyncConflictResolvers.remoteWins(),
                                null,
                                null
                        );
                    });
        }
        // end MongoDB setup stuff

        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            startActivity(new Intent(this, SearchActivity.class));
        });

        Button addBookButton = findViewById(R.id.addBookButton);
        addBookButton.setOnClickListener(v -> {
            startActivity(new Intent(this, BookAddActivity.class));
        });
    }

    /**
     * Called when activity is finished
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Getter for mongoCollection
     * @return Our usable collection
     */
    public static RemoteMongoCollection getMongoCollection() {
        return mongoCollection;
    }

    /**
     * Getter for mongoClient
     * @return Our usable client
     */
    public static RemoteMongoClient getMongoClient() {
        return mongoClient;
    }
}
