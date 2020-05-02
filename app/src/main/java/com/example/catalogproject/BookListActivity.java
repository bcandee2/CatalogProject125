package com.example.catalogproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.catalogproject.Logic.Book;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private final RemoteMongoCollection mongoCollection = MainActivity.getMongoCollection();
    private final RemoteMongoClient mongoClient = MainActivity.getMongoClient();
    private ArrayList<Book> books;
    private ArrayList<Book> sortedBooks;
    private String sortField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        Bundle bundle = getIntent().getExtras();
        books = (ArrayList<Book>) bundle.getSerializable("books");
        sortedBooks = Book.sortBy(books, "title");

        updateBooksUi();

        Spinner sortSpinner = findViewById(R.id.sortSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sortSpinner.setAdapter(adapter);
        sortSpinner.setOnItemSelectedListener(this);
        Button sortButton = findViewById(R.id.sortButton);
        sortButton.setOnClickListener(v -> {
            sortedBooks = Book.sortBy(books, sortField);
            updateBooksUi();
        });

    }

    public void updateBooksUi() {
        LinearLayout booksLayout = findViewById(R.id.listLayout);
        booksLayout.removeAllViews();
        for (Book b : sortedBooks) {
            View chunk = getLayoutInflater().inflate(R.layout.chunk_addedbook, booksLayout, false);
            TextView text = chunk.findViewById(R.id.bookInfoText);
            text.setText(b.toString());

            Button infoButton = chunk.findViewById(R.id.bookInfoButton);
            infoButton.setOnClickListener(v -> {
                Intent infoIntent = new Intent(this, BookInfoActivity.class);
                Bundle extras = new Bundle();
                extras.putSerializable("book", b);
                infoIntent.putExtras(extras);
                startActivity(infoIntent);
                finish();
            });

            Button removeButton = chunk.findViewById(R.id.bookRemoveButton);
            removeButton.setOnClickListener(v -> {
                books.remove(b);
                sortedBooks.remove(b);
                updateBooksUi();
            });
            booksLayout.addView(chunk);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sortField = parent.getItemAtPosition(position).toString().toLowerCase();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        sortField = "title";
    }
}
