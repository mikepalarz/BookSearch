package com.palarz.mike.booksearch;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.HTTP;

public class BookListActivity extends AppCompatActivity {

    private static final String TAG = BookListActivity.class.getSimpleName();

    private ListView mBookList;
    private BookAdapter mAdapter;
    private BookClient mClient;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        mBookList = (ListView) findViewById(R.id.book_list_list_view);
        ArrayList<Book> books = new ArrayList<>();
        mAdapter = new BookAdapter(this, books);
        mBookList.setAdapter(mAdapter);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    private void fetchBooks(String query) {
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BookClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mClient = retrofit.create(BookClient.class);
        Call<ResponseBody> call = mClient.getAllBooks(query);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "The full URL: " + response.toString());
                try {
                    if (response != null) {
                        /*
                        response.body().string() returns the contents of the body of the HTTP
                        response, encoded with the default character set. In other words,
                        this returns us the entire contents of the JSON data.
                         */
                        JSONObject jsonRoot = new JSONObject(response.body().string());

                        // We then parse through the JSON data and add the books to the adapter
                        JSONArray docs = jsonRoot.getJSONArray("docs");
                        ArrayList<Book> books = Book.fromJson(docs);
                        mAdapter.clear();
                        for (Book book : books) {
                            mAdapter.add(book);
                        }
                        mAdapter.notifyDataSetChanged();
                        mProgressBar.setVisibility(ProgressBar.GONE);
                    }

                } catch (IOException exception) {
                    exception.printStackTrace();
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: The call object's toString():" + call.request().toString());
                mProgressBar.setVisibility(ProgressBar.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Fetch the remote data
                fetchBooks(query);
                // Reset the SearchView
                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();
                // We'll also set the title of the activity to the current search query
                BookListActivity.this.setTitle(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }
}
