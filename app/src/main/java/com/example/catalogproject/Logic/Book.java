package com.example.catalogproject.Logic;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Book implements Serializable {

    /**
     * The title of the book
     */
    private String title;

    /**
     * The author of the book
     */
    private String author;

    /**
     * The genre of the book
     */
    private Genre genre;

    /**
     * The book description
     */
    private String description;

    /**
     * The document version of the book
     */
    private Document document;

    /**
     * The id of the book
     */
    private String id;

    /**
     * Takes a JSONObject and converts it to a book
     * @param jsonBook JSONObject book
     */
    public Book(JSONObject jsonBook)  {
        try {
            title = jsonBook.getString("title");
            author = jsonBook.getString("author");
            description = jsonBook.getString("description");
            genre = Genre.valueOf(jsonBook.getString("genre").toUpperCase());
            id = jsonBook.getString("id");
        } catch (IllegalArgumentException il) {
            genre = Genre.UNKNOWN;
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Takes a document and converts it to a book
     * @param docBook Document book
     */
    public Book(Document docBook) {
        title = docBook.getString("title");
        author = docBook.getString("author");
        description = docBook.getString("description");
        genre = Genre.valueOf(docBook.getString("genre"));
        document = docBook;
    }

    /**
     * Getter for title
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for author
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Getter for description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for id
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Getter for genre
     * @return genre
     */
    public Genre getGenre() {
        return genre;
    }

    /**
     * Getter for genre as String
     * @return genre as String
     */
    public String getGenreAsString() {
        return genre.toString();
    }

    /**
     * Converts book to String
     * @return Title and author
     */
    public String toString() {
        return title + ": " + author;
    }

    /**
     * Gets book as a document
     * @return Document book
     */
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

    /**
     * Sorts an array list of books by a field
     * @param books list to sort
     * @param field field to sort by
     * @return A new sorted array list
     */
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

    /**
     * Swaps 2 items in an array
     * @param values the array
     * @param first first value
     * @param second second value
     */
    public static void swap(Object[] values, int first, int second) {
        Object temp = values[first];
        values[first] = values[second];
        values[second] = temp;
    }

    /**
     * Creates a new books list given the sorted indicies
     * @param books Original books list
     * @param indices Array of indicies
     * @return Sorted books list
     */
    public static ArrayList<Book> newBooksList(ArrayList<Book> books, Integer[] indices) {
        ArrayList<Book> toReturn = new ArrayList<>();
        for (Integer i: indices) {
            toReturn.add(books.get(i));
        }
        return toReturn;
    }
}
