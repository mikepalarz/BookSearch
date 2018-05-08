package com.palarz.mike.booksearch;

/**
 * Created by mike on 5/8/18.
 */

public class Book {

    private String openLibraryId;
    private String author;
    private String title;

    public String getOpenLibraryId() {
        return openLibraryId;
    }

    public String getAuthor() {
        return author;
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

}
