package com.example.catalogproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.catalogproject.Logic.Book;
import com.example.catalogproject.Logic.RequestCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;
import com.mongodb.stitch.core.services.mongodb.remote.sync.DefaultSyncConflictResolvers;
import com.mongodb.stitch.android.services.http.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static RemoteMongoCollection mongoCollection;
    private static RemoteMongoClient mongoClient;
    private ArrayList<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // MongoDB setup stuff
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

    public static RemoteMongoCollection getMongoCollection() {
        return mongoCollection;
    }

    public static RemoteMongoClient getMongoClient() {
        return mongoClient;
    }
}
