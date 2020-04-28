package com.example.catalogproject.Logic;

import org.json.JSONException;
import org.json.JSONObject;

public class Book {
    private String title;
    private String author;
    private Genre genre;
    private String description;
    public Book(JSONObject jsonBook)  {
        try {
            title = jsonBook.getString("title");
            author = jsonBook.getString("author");
            switch (jsonBook.getString("genre").toLowerCase()) {
                case "biography":
                    genre = Genre.BIOGRAPHY;
                    break;
                case "fantasy":
                    genre = Genre.FANTASY;
                    break;
                case "horror":
                    genre = Genre.HORROR;
                    break;
                case "mystery":
                    genre = Genre.MYSTERY;
                    break;
                default:
                    genre = Genre.UNKNOWN;
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
}
