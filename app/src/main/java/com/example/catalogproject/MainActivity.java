package com.example.catalogproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
            startActivityForResult(new Intent(this, BookAddActivity.class), RequestCodes.REQUEST_SEARCH);
            if (books != null) {
                startActivity(new Intent(this, BookListActivity.class));
            } else {
                System.out.println("here");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCodes.REQUEST_SEARCH) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                books = (ArrayList<Book>) bundle.getSerializable("books");
            }
        } else if (requestCode == RequestCodes.REQUEST_ADD) {
            if (resultCode ==  RESULT_OK) {
                System.out.print("Great!");
            }
        }
    }
}
