package com.example.catalogproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.catalogproject.Logic.Book;
import com.google.android.gms.tasks.Task;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.android.services.mongodb.remote.SyncFindIterable;

import org.bson.Document;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for searching for a given book.
 * Searches for documents with all fields matching (case-insensitive)
 * except for description, which matches any keywords listed.
 */
public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    /**
     * The RemoteMongoCollection created in MainActivity for use making search requests.
     */
    private RemoteMongoCollection mongoCollection = MainActivity.getMongoCollection();

    /**
     * The list of books to load search results into and pass to BookListActivity.
     */
    private static ArrayList<Book> books = new ArrayList<>();

    /**
     * The currently selected genre (or "None").
     */
    String genre;

    /**
     * Called by system when activity is being initialized.
     * @param savedInstanceState (unused) the information from the previous instance of SearchActivity.
     */
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

            //Get our user inputted search
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
                obj.append("$options", "i"); // Case insensitive
                search.add(new BasicDBObject("title", obj));
                empty = false;
            }
            if (author.length() > 0) {
                obj = new BasicDBObject("$regex", author);
                obj.append("$options", "i"); // Case insensitive
                search.add(new BasicDBObject("author", obj));
                empty = false;
            }
            if  (description.length() > 0) {
                List<DBObject> keywords = new ArrayList<>();
                for (String keyword: description.split("[ ]+")) {
                    obj = new BasicDBObject("$regex", keyword);
                    obj.append("$options", "i"); // Case insensitive
                    keywords.add(new BasicDBObject("description", obj));
                    empty = false;
                }
                search.add(new BasicDBObject("$or", keywords)); // Match any keyword
            }
            if (!genre.equals("None")) {
                obj = new BasicDBObject("$regex", genre);
                obj.append("$options", "i"); // Case insensitive
                search.add(new BasicDBObject("genre", obj));
                empty = false;
            }

            if (!empty) {
                // If user inputted anything, make query document with search fields
                query.put("$and", search);
                Log.d("Bookie", query.toJson());
            } else {
                // Else find all DB documents);
                Log.d("Bookie", "searching all");
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
                    String bookId = item.getObjectId("_id").toHexString();
                    String bookTitle = item.getString("title");
                    String bookAuthor = item.getString("author");
                    String bookGenre = item.getString("genre");
                    String bookDesc = item.getString("description");
                    try {
                            JSONObject searchedBook = new JSONObject()
                                    .put("title", bookTitle)
                                    .put("author", bookAuthor)
                                    .put("genre", bookGenre)
                                    .put("description", bookDesc)
                                    .put("id", bookId);
                        books.add(new Book(searchedBook));
                    } catch (Exception e) {
                        Log.e("Bookie", "Couldn't parse document");
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("books", books);
                Intent listIntent = new Intent(getApplicationContext(), BookListActivity.class);
                listIntent.putExtras(bundle);
                startActivity(listIntent);
                Log.d("Bookie", searchResult.toString());
            });
        });
    }

    /**
     * What to do with the selected spinner item.
     * @param parent the parent activity
     * @param view the parent view
     * @param position the on-screen position of the item
     * @param id the id of the item selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        genre = parent.getItemAtPosition(position).toString();
    }

    /**
     * (Unused) What to do when no spinner item is selected.
     * @param parent the parent activity
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }
}
