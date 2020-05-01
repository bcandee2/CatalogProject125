package com.example.catalogproject.Logic;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Book implements Serializable {
    private String title;
    private String author;
    private Genre genre;
    private String description;
    private Document document;
    public Book(JSONObject jsonBook)  {
        try {
            title = jsonBook.getString("title");
            author = jsonBook.getString("author");
            description = jsonBook.getString("description");
            genre = Genre.valueOf(jsonBook.getString("genre").toUpperCase());
        } catch (IllegalArgumentException il) {
            genre = Genre.UNKNOWN;
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
    public Book(Document docBook) {
        title = docBook.getString("title");
        author = docBook.getString("author");
        description = docBook.getString("description");
        genre = Genre.valueOf(docBook.getString("genre"));
        document = docBook;
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

    public Document getDocument() {
        if (document != null) {
            return document;
        } else {
            document = new Document();
            document.append("title", title);
            document.append("author", author);
            document.append("genre", getGenreAsString().toLowerCase());
            document.append("description", description);
            return document;
        }
    }

    public static ArrayList<Book> sortBy(ArrayList<Book> books, String field) {
        if (books == null || books.size() < 2) {
            return books;
        }
        String[] values = new String[books.size()];
        Integer[] indices = new Integer[books.size()];
        switch (field) {
            case "title":
                for (int i = 0; i < values.length; i++) {
                    values[i] = books.get(i).getTitle();
                    indices[i] = i;
                }
                break;
            case "author":
                for (int i = 0; i < values.length; i++) {
                    values[i] = books.get(i).getAuthor();
                    indices[i] = i;
                }
                break;
            case "genre":
                for (int i = 0; i < values.length; i++) {
                    values[i] = books.get(i).getGenreAsString();
                    indices[i] = i;
                }
                break;
            default:
                sortBy(books, "title");
        }
        for (int i = 1; i < values.length; i++) {
            int j = i;
            while (values[j].compareTo(values[j - 1]) < 0) {
                swap(values, j, j - 1);
                swap(indices, j, j - 1);
                j--;
                if (j == 0) {
                    break;
                }
            }
        }
        return newBooksList(books, indices);
    }

    public static void swap(Object[] values, int first, int second) {
        Object temp = values[first];
        values[first] = values[second];
        values[second] = temp;
    }

    public static ArrayList<Book> newBooksList(ArrayList<Book> books, Integer[] indices) {
        ArrayList<Book> toReturn = new ArrayList<>();
        for (Integer i: indices) {
            toReturn.add(books.get(i));
        }
        return toReturn;
    }
}
