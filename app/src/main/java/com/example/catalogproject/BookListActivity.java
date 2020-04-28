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

        LinearLayout booksLayout = findViewById(R.id.listLayout);
        for (Book b: books) {
            View chunk = getLayoutInflater().inflate(R.layout.chunk_book, booksLayout, false);
            TextView text = chunk.findViewById(R.id.bookInfoText);
            text.setText(b.toString());

            Button infoButton = chunk.findViewById(R.id.bookInfoButton);
            if (infoButton == null) {
                Log.d("Bookie", "null button " + b.toString());
            }
            infoButton.setOnClickListener(v -> {
                Intent infoIntent = new Intent(this, BookInfoActivity.class);
                Bundle extras = new Bundle();
                extras.putSerializable("book", b);
                infoIntent.putExtras(extras);
                startActivity(infoIntent);
            });
            booksLayout.addView(chunk);
        }
    }

    protected void updateBooksUi() {
        LinearLayout booksLayout = findViewById(R.id.listLayout);
        booksLayout.removeAllViews();
        for (Book b: books) {
            View chunk = getLayoutInflater().inflate(R.layout.chunk_book, booksLayout, true);
            TextView text = chunk.findViewById(R.id.bookInfoText);
            text.setText(b.toString());

            Button removeButton = findViewById(R.id.bookInfoButton);
            removeButton.setOnClickListener(v -> {
                Intent infoIntent = new Intent(this, BookInfoActivity.class);
                Bundle extras = new Bundle();
                extras.putSerializable("book", b);
                infoIntent.putExtras(extras);
                startActivity(infoIntent);
            });
        }
    }
}
