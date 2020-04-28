package com.example.catalogproject.Logic;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Book implements Serializable {
    private String title;
    private String author;
    private Genre genre;
    private String description;
    public Book(JSONObject jsonBook)  {
        try {
            title = jsonBook.getString("title");
            author = jsonBook.getString("author");
            genre = Genre.valueOf(jsonBook.getString("genre").toUpperCase());
            description = jsonBook.getString("description");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public Genre getGenre() {
        return genre;
    }

    public String getGenreAsString() {
        return genre.toString();
    }

    public String toString() {
        return title + ": " + author;
    }
}
