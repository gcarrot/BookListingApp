package si.gcarrot.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>>,
        SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String LOG_TAG = MainActivity.class.getName();

    /** API URL */
    //?q=android&maxResults=4
    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes";

    private static final int LOADER_ID = 1;

    /** Adapter for the list */
    private BookAdapter mAdapter;

    /** Empty list */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyStateTextView);


        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        listView.setAdapter(mAdapter);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        final View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);


        // Update empty state with no first search error message
        mEmptyStateTextView.setText(R.string.no_start_search);

        Button btn_search = (Button) findViewById(R.id.btn_search);

        btn_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText et_search = (EditText) findViewById(R.id.et_search);
                String search_text = et_search.getText().toString();

                // Check if string is empty
                if(!search_text.isEmpty()){

                    mAdapter.clear();
                    getLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);

                    loadingIndicator.setVisibility(View.VISIBLE);
                    mEmptyStateTextView.setVisibility(View.GONE);

                    // Get a reference to the ConnectivityManager to check state of network connectivity
                    ConnectivityManager connMgr = (ConnectivityManager)
                            getSystemService(Context.CONNECTIVITY_SERVICE);


                    // Get details on the currently active default data network
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                    // If there is a network connection, fetch data
                    if (networkInfo != null && networkInfo.isConnected()) {
                        // Get a reference to the LoaderManager, in order to interact with loaders.
                        LoaderManager loaderManager = getLoaderManager();
                        loaderManager.initLoader(LOADER_ID, null, MainActivity.this);
                    } else {
                        View loadingIndicator = findViewById(R.id.loading_indicator);
                        loadingIndicator.setVisibility(View.GONE);

                        // Update empty state with no connection error message
                        mEmptyStateTextView.setText(R.string.no_internet_connection);
                    }
                } else {
                    mAdapter.clear();
                    // Update empty state with empty search string error message
                    mEmptyStateTextView.setText(R.string.empty_search);
                }
            }
        });

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        mAdapter.clear();

        mEmptyStateTextView.setVisibility(View.GONE);

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);

        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {


        Uri baseUri = Uri.parse(API_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        EditText et_search = (EditText) findViewById(R.id.et_search);
        String search = et_search.getText().toString();

        uriBuilder.appendQueryParameter("q", search);
        uriBuilder.appendQueryParameter("maxResults", "10");

        Log.i(LOG_TAG, "Url: " + uriBuilder.toString());

        return new BookLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mAdapter.clear();

        mEmptyStateTextView.setText(R.string.no_books);
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);

            mEmptyStateTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }


}
