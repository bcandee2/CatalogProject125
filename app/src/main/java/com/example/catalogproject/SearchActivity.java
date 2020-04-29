package com.example.catalogproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.catalogproject.Logic.Book;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    //private RemoteMongoCollection mongoCollection = MainActivity.getMongoCollection();
    //private RemoteMongoClient mongoClient = MainActivity.getMongoClient();
    public ArrayList<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            // Initialize books array
            books = new ArrayList<>();
            // First make search request to Kinvey

            // Start books test
            try {
                JSONObject book1 = new JSONObject();
                book1.put("title", "Fahrenheit 451");
                book1.put("author", "Ray Bradbury");
                book1.put("genre", "horror");
                book1.put("description", "A thrilling tale of a criminal who steals books " +
                        "and the firemen who just want them back");
                books.add(new Book(book1));
                JSONObject book2 = new JSONObject();
                book2.put("title", "Freakonomics");
                book2.put("author", "Malcom Gladwell");
                book2.put("genre", "fantasy");
                book2.put("description", "A wonderful representation of what the world " +
                        "would be like if economics really mattered");
                books.add(new Book(book2));
            } catch (JSONException ex) {
                Log.d("JSONException", "books test failed");
            }
            // End books test
            Bundle bundle = new Bundle();
            bundle.putSerializable("books", books);
            Intent listIntent = new Intent(this, BookListActivity.class);
            listIntent.putExtras(bundle);
            startActivity(listIntent);
        });
    }
}
