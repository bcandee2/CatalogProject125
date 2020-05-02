package com.example.catalogproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.catalogproject.Logic.Book;

import java.util.ArrayList;

/**
 * Activity to list the books passed to it by the parent SearchActivity.
 */
public class BookListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    /**
     * The unsorted list of books from the parent SearchActivity.
     */
    private ArrayList<Book> books;

    /**
     * The list of books sorted by a given field.
     */
    private ArrayList<Book> sortedBooks;

    /**
     * The currently selected field to sort by.
     */
    private String sortField;

    /**
     * Called by system when activity is being initialized.
     * @param savedInstanceState (unused) the information from the previous instance of BookListActivity.
     */
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

    /**
     * Sets up/refreshes the list of books currently displayed.
     */
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
            booksLayout.addView(chunk);
        }
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
        sortField = parent.getItemAtPosition(position).toString().toLowerCase();
    }

    /**
     * (Unused) What to do when no spinner item is selected.
     * @param parent the parent activity
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        sortField = "title";
    }
}
