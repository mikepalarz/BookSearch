package com.palarz.mike.booksearch;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 5/14/18.
 */

public class BookSearchResponse {

    @SerializedName("start")
    private int mStart;
    @SerializedName("num_found")
    private int mNumberFound;
    @SerializedName("docs")
    private List<Book> mBooks;

    public BookSearchResponse() {
        this.mStart = 0;
        this.mNumberFound = 0;
        this.mBooks = new ArrayList<>();
    }

    public BookSearchResponse(int start, int numberFound, ArrayList<Book> books) {
        this.mStart = start;
        this.mNumberFound = numberFound;
        this.mBooks = books;
    }

    public int getStart() {
        return mStart;
    }

    public void setStart(int mStart) {
        this.mStart = mStart;
    }

    public int getNumberFound() {
        return mNumberFound;
    }

    public void setNumberFound(int mNumberFound) {
        this.mNumberFound = mNumberFound;
    }

    public List<Book> getBooks() {
        return mBooks;
    }

    public void setBooks(List<Book> mBooks) {
        this.mBooks = mBooks;
    }

}
