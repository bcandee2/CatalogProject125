package com.example.catalogproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.catalogproject.Logic.Book;
import com.example.catalogproject.Logic.RequestCodes;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            startActivity(new Intent(this, SearchActivity.class));
        });

        Button addBookButton = findViewById(R.id.addBookButton);
        addBookButton.setOnClickListener(v -> {
            startActivity(new Intent(this, BookAddActivity.class));
        });
    }
}
