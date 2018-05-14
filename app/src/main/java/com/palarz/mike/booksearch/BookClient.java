package com.palarz.mike.booksearch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by mike on 5/8/18.
 */

public interface BookClient {

    String BASE_URL = "http://openlibrary.org/";

    @GET("search.json")
    Call<BookSearchResponse> getAllBooks(@Query("q") String query);

}
