package com.palarz.mike.booksearch;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by mike on 5/8/18.
 */

public interface BookClient {

    String BASE_URL = "http://openlibrary.org/";

    @GET("search.json")
    Call<ResponseBody> getAllBooks(@Query("q") String query);

}
