package com.example.catalogproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.catalogproject.Logic.Book;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class BookAddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String genre;
    private RemoteMongoCollection mongoCollection = MainActivity.getMongoCollection();
    private RemoteMongoClient mongoClient = MainActivity.getMongoClient();
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
                    } else {
                        Log.e("Bookie", "shit");
                    }
                });
            } catch (JSONException ex) {
                Log.d("Bookie", "add book test failed");
            }
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
