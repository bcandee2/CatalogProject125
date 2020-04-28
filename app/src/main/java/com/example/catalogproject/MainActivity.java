package com.example.catalogproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            startActivity(new Intent(this, SearchActivity.class));
            finish();
        });

        Button addBookButton = findViewById(R.id.addBookButton);
        addBookButton.setOnClickListener(v -> {
            startActivity(new Intent(this, BookAddActivity.class));
            finish();
        });
    }
}
