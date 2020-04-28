package com.example.catalogproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.catalogproject.Logic.Book;

import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity {
    private ArrayList<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
    }
}
