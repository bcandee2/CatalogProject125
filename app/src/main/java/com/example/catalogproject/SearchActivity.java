package com.example.catalogproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.catalogproject.Logic.Book;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.Block;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.services.StitchServiceClient;
import com.mongodb.stitch.android.services.http.HttpServiceClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.android.services.mongodb.remote.SyncFindIterable;
import com.mongodb.stitch.core.services.http.HttpMethod;
import com.mongodb.stitch.core.services.http.HttpRequest;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private RemoteMongoCollection mongoCollection = MainActivity.getMongoCollection();
    private RemoteMongoClient mongoClient = MainActivity.getMongoClient();
    public ArrayList<Book> books = new ArrayList<>();
    public ArrayList<Document> results;
    String genre;
    private JSONParser parser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Spinner genreSpinner = (Spinner) findViewById(R.id.genreSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genre_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        genreSpinner.setAdapter(adapter);
        genreSpinner.setOnItemSelectedListener(this);

        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            EditText titleEditText = findViewById(R.id.titleEditText3);
            EditText authorEditText = findViewById(R.id.authorEditText3);
            EditText descEditText = findViewById(R.id.descriptionEditText3);

            //Get our user inputed search
            String title = titleEditText.getText().toString();
            String author = authorEditText.getText().toString();
            String selectedGenre = genre;
            String description = descEditText.getText().toString();


            // Start books test
            /*try {
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
            }*/
            // End books test

            Log.d("Bookie", "Search activity started");
            Document query = new Document("title", new Document("$exists", true));
            SyncFindIterable<Document> results = mongoCollection.sync().find(query);
            List<Document> searchResult = new ArrayList<>();
            Task<List<Document>> finished = results.into(searchResult);
            finished.addOnCompleteListener(new OnCompleteListener<List<Document>>() {
                @Override
                public void onComplete(@NonNull Task<List<Document>> task) {
                    for (Document item : searchResult) {
                        String bookTitle = item.getString("title");
                        String bookAuthor = item.getString("author");
                        String bookGenre = item.getString("genre");
                        String bookDesc = item.getString("description");
                        try {
                            JSONObject searchedBook = new JSONObject()
                                    .put("title", bookTitle)
                                    .put("author", bookAuthor)
                                    .put("genre", bookGenre)
                                    .put("description", bookDesc);
                            books.add(new Book(searchedBook));
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("books", books);
                            Intent listIntent = new Intent(getApplicationContext(), BookListActivity.class);
                            listIntent.putExtras(bundle);
                            startActivity(listIntent);
                        } catch (JSONException e) {
                            Log.e("Bookie", "Couldn't parse document");
                        }
                    }
                    Log.d("Bookie", books.toString());
                }
            });
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        genre = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }
}
