package com.example.catalogproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.catalogproject.Logic.Book;

public class BookInfoActivity extends AppCompatActivity {
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        Bundle bundle = getIntent().getExtras();
        book = (Book) bundle.getSerializable("book");

        TextView titleAuthor = findViewById(R.id.titleAuthorView);
        titleAuthor.setText(book.toString());

        TextView genre = findViewById(R.id.genreView);
        genre.setText(book.getGenreAsString());

        TextView description = findViewById(R.id.descriptionView);
        description.setText(book.getDescription());
    }
}
