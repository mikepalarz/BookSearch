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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        Call<BookSearchResponse> call = mClient.getAllBooks(query);
        call.enqueue(new Callback<BookSearchResponse>() {
            @Override
            public void onResponse(Call<BookSearchResponse> call, Response<BookSearchResponse> response) {
                Log.d(TAG, "The full URL: " + response.toString());
                BookSearchResponse bookSearchResponse = response.body();
                if (response.isSuccessful()) {
                    mAdapter.clear();
                    List<Book> books = bookSearchResponse.getBooks();
                    for (Book book : books) {
                        mAdapter.add(book);
                    }
                    mAdapter.notifyDataSetChanged();
                    mProgressBar.setVisibility(ProgressBar.GONE);
                }
            }

            @Override
            public void onFailure(Call<BookSearchResponse> call, Throwable t) {
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
