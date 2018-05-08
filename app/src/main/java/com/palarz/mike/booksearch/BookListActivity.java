package com.palarz.mike.booksearch;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.ProgressBar;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class BookListActivity extends AppCompatActivity {

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

        mClient = new BookClient();
        mClient.getBooks(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray docs = null;
                    if (response != null) {
                        docs = response.getJSONArray("docs");
                        final ArrayList<Book> books = Book.fromJson(docs);
                        mAdapter.clear();
                        for (Book book : books) {
                            mAdapter.add(book);
                        }
                        mAdapter.notifyDataSetChanged();
                        mProgressBar.setVisibility(ProgressBar.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
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
