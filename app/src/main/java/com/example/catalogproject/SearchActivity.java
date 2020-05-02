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
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBObject;
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

        // Load text inputs
        EditText titleEditText = findViewById(R.id.titleEditText3);
        EditText authorEditText = findViewById(R.id.authorEditText3);
        EditText descEditText = findViewById(R.id.descriptionEditText3);


        Spinner genreSpinner = findViewById(R.id.genreSpinner);
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

            //Get our user inputed search
            String title = titleEditText.getText().toString();
            String author = authorEditText.getText().toString();
            String description = descEditText.getText().toString();

            // Make query document
            BasicDBObject query = new BasicDBObject();
            List<DBObject> search = new ArrayList<>();
            BasicDBObject obj;
            boolean empty = true;
            if (title.length() > 0) {
                obj = new BasicDBObject("$regex", title);
                obj.append("$options", "i");
                search.add(new BasicDBObject("title", obj));
                empty = false;
            }
            if (author.length() > 0) {
                obj = new BasicDBObject("$regex", author);
                obj.append("$options", "i");
                search.add(new BasicDBObject("author", obj));
                empty = false;
            }
            if  (description.length() > 0) {
                List<DBObject> keywords = new ArrayList<>();
                for (String keyword: description.split("[ ]+")) {
                    obj = new BasicDBObject("$regex", keyword);
                    obj.append("$options", "i");
                    keywords.add(new BasicDBObject("description", obj));
                    empty = false;
                }
                search.add(new BasicDBObject("$or", keywords));
            }
            if (!genre.equals("None")) {
                obj = new BasicDBObject("$regex", genre);
                obj.append("$options", "i");
                search.add(new BasicDBObject("genre", obj));
                empty = false;
            }

            if (!empty) {
                // If user inputted anything, make query document with search fields
                query.put("$and", search);
                Log.d("Bookie", query.toJson());
            } else {
                // Else find all DB documents
                query.put("title", new BasicDBObject("$exists", true));
                Log.e("Bookie", "uh oh");
            }

            // clear books list
            books.clear();

            Log.d("Bookie", "Search activity started");
            SyncFindIterable<Document> results = mongoCollection.sync().find(query);
            List<Document> searchResult = new ArrayList<>();
            Task<List<Document>> finished = results.into(searchResult);
            finished.addOnCompleteListener(task -> {
                books.clear();
                for (Document item : searchResult) {
                    String bookTitle = item.getString("title");
                    String bookAuthor = item.getString("author");
                    String bookGenre = item.getString("genre");
                    String bookDesc = item.getString("description");
                    try {
                        JSONObject searchedBook = new JSONObject().put("title", bookTitle)
                                .put("author", bookAuthor)
                                .put("genre", bookGenre)
                                .put("description", bookDesc);
                        books.add(new Book(searchedBook));
                        Log.d("Bookie", "Book added");
                        Log.d("Bookie", books.toString());
                    } catch (Exception e) {
                        Log.e("Bookie", "Couldn't parse document");
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("books", books);
                Intent listIntent = new Intent(getApplicationContext(), BookListActivity.class);
                listIntent.putExtras(bundle);
                Toast.makeText(getApplicationContext(), "Final List: " + books.toString(), Toast.LENGTH_LONG).show();
                startActivity(listIntent);
                Log.d("Bookie", searchResult.toString());
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
