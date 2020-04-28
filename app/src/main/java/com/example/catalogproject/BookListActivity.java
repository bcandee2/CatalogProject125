package com.example.catalogproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.catalogproject.Logic.Book;

import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity {
    private ArrayList<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        Bundle bundle = getIntent().getExtras();
        books = (ArrayList<Book>) bundle.getSerializable("books");

        LinearLayout booksLayout = findViewById(R.id.listLayout);
        for (Book b: books) {
            View chunk = getLayoutInflater().inflate(R.layout.chunk_book, booksLayout, true);
            TextView text = chunk.findViewById(R.id.bookInfo);
            text.setText(b.toString());

            Button removeButton = findViewById(R.id.removeBook);
            removeButton.setOnClickListener(v -> {
                books.remove(b);
                updateBooksUi();
            });
        }
    }

    protected void updateBooksUi() {
        LinearLayout booksLayout = findViewById(R.id.listLayout);
        booksLayout.removeAllViews();
        for (Book b: books) {
            View chunk = getLayoutInflater().inflate(R.layout.chunk_book, booksLayout, true);
            TextView text = chunk.findViewById(R.id.bookInfo);
            text.setText(b.toString());

            Button removeButton = findViewById(R.id.removeBook);
            removeButton.setOnClickListener(v -> {
                books.remove(b);
                updateBooksUi();
            });
        }
    }
}
