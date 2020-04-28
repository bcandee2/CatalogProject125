package com.example.catalogproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

        updateBooksUi();
    }

    protected void updateBooksUi() {
        LinearLayout booksLayout = findViewById(R.id.listLayout);
        booksLayout.removeAllViews();
        for (Book b: books) {
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
            });

            Button removeButton = chunk.findViewById(R.id.bookRemoveButton);
            removeButton.setOnClickListener(v -> {
                books.remove(b);
                updateBooksUi();
            });
            booksLayout.addView(chunk);
        }
    }
}
