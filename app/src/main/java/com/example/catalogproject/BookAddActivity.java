package com.example.catalogproject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.catalogproject.Logic.Book;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity for adding a new book to the collection.
 * You must add a book before search will return any results.
 */
public class BookAddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    /**
     * The currently selected genre.
     */
    private String genre;

    /**
     * The RemoteMongoCollection created in MainActivity for use making add requests.
     */
    private RemoteMongoCollection mongoCollection = MainActivity.getMongoCollection();

    /**
     * Called by system when activity is being initialized.
     * @param savedInstanceState (unused) the information from the previous instance of BookAddActivity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_add);

        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText authorEditText = findViewById(R.id.authorEditText);
        EditText descEditText = findViewById(R.id.descriptionEditText);
        Button addBookButton = findViewById(R.id.addBookButton);
        Spinner genreSpinner = (Spinner) findViewById(R.id.genreSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genre_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        genreSpinner.setAdapter(adapter);
        genreSpinner.setOnItemSelectedListener(this);

        addBookButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String author = authorEditText.getText().toString();
            String selectedGenre = genre;
            String description = descEditText.getText().toString();
            // Send add request to MongoDB here
            try {
                JSONObject newJsonBook = new JSONObject();
                newJsonBook.put("title", title);
                newJsonBook.put("author", author);
                newJsonBook.put("genre", selectedGenre);
                newJsonBook.put("description", description);
                Book newBook = new Book(newJsonBook);
                final Task<RemoteInsertOneResult> insertTask = mongoCollection.sync().insertOne(newBook.getDocument());
                insertTask.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Bookie", "We did it boys!");
                        Context context = getApplicationContext();
                        Toast.makeText(context, "Book sumbitted!", Toast.LENGTH_LONG).show();
                        titleEditText.setText("");
                        authorEditText.setText("");
                        descEditText.setText("");
                    } else {
                        Log.e("Bookie", "shit");
                    }
                });
            } catch (JSONException ex) {
                Log.d("Bookie", "add book test failed");
            }
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
