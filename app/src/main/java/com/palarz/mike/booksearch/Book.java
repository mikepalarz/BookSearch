package com.palarz.mike.booksearch;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mike on 5/8/18.
 */

public class Book {

    @SerializedName("cover_edition_key")
    private String openLibraryId;

    @SerializedName("author_name")
    private String [] authors;

    @SerializedName("title_suggest")
    private String title;

    private String authorDisplay;

    public String getOpenLibraryId() {
        return openLibraryId;
    }

    public String [] getAuthors() {
        return authors;
    }

    public String getTitle() {
        return title;
    }

    // Returns the URL for a medium-sized image of the book cover
    public String getCoverUrl() {
        return "http://covers.openlibrary.org/b/olid/" + openLibraryId + "-M.jpg?default=false";
    }

    // Returns the URL for a medium-sized image of the book cover
    public String getLargeCoverUrl() {
        /*
        By appending ?default=false to the URL, the API will return a 404 message if the book cover
        is not found. This actually works to our benefit because we will be supplying our own image
        in place of missing book covers.
        */
        return "http://covers.openlibrary.org/b/olid/" + openLibraryId + "-L.jpg?default=false";
    }

    public String getAuthorDisplay() {
        return authorDisplay;
    }

    public Book() {
        this.openLibraryId = "";
        this.title = "";
        this.authors = new String[] {};
        this.authorDisplay = "";
    }

    public Book(String openLibraryId, String title, String[] authors, String authorDisplay) {
        this.openLibraryId = openLibraryId;
        this.title = title;
        this.authors = authors;
        authorDisplay = TextUtils.join(", ", this.authors);
    }

    public static Book fromJson(JSONObject jsonObject) {
        Book book = new Book();
        try {
            // We are deserializing the JSON response into object fields
            // First, we check if the cover edition is available
            if (jsonObject.has("cover_edition_key")) {
                book.openLibraryId = jsonObject.getString("cover_edition_key");
            } else if (jsonObject.has("edition_key")) {
                final JSONArray ids = jsonObject.getJSONArray("edition_key");
                book.openLibraryId = ids.getString(0);
            }
            book.title = jsonObject.has("title_suggest") ? jsonObject.getString("title_suggest") : "";
//            book.authors = getAuthor(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        // Return the newly adjusted Book object
        return book;
    }

    // Method which returns a comma-separated list of authors when there is more than one
    private static String getAuthor(final JSONObject jsonObject) {
        try {
            final JSONArray authors = jsonObject.getJSONArray("author_name");
            int authorsCount = authors.length();
            final String[] authorStrings = new String[authorsCount];
            for (int i = 0; i < authorsCount; i++) {
                authorStrings[i] = authors.getString(i);
            }
            return TextUtils.join(", ", authorStrings);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static ArrayList<Book> fromJson(JSONArray jsonArray) {
        ArrayList<Book> books = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject bookJson = null;
            try {
                bookJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Book book = fromJson(bookJson);
            if (book != null) {
                books.add(book);
            }
        }
        return books;
    }

}
