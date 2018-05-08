package com.palarz.mike.booksearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        mBookList = (ListView) findViewById(R.id.book_list_list_view);
        ArrayList<Book> books = new ArrayList<>();
        mAdapter = new BookAdapter(this, books);
        mBookList.setAdapter(mAdapter);

        fetchBooks();
    }

    private void fetchBooks() {
        mClient = new BookClient();
        mClient.getBooks("oscar Wilde", new JsonHttpResponseHandler() {
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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
