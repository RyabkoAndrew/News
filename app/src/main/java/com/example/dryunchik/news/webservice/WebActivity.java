package com.example.dryunchik.news.webservice;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dryunchik.news.R;
import com.example.dryunchik.news.data.NewsContract;

import java.net.URL;

public class WebActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private Uri mCurrentUriNews;
    private static final int URL_LOADERS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        mCurrentUriNews = getIntent().getData();

        String url = getIntent().getStringExtra(Intent.EXTRA_REFERRER);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBarHorizontal);
        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.setWebViewClient(new MyWebClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(progress);
                setTitle("Loading...");

                if (progress == 100) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    setTitle(view.getTitle());
                }
                super.onProgressChanged(view, progress);

            }
        });
        if (mCurrentUriNews == null) {
            mWebView.loadUrl(url);

        } else {
            getLoaderManager().initLoader(URL_LOADERS, null, this);
        }


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projections = {NewsContract.NewsEntry._ID, NewsContract.NewsEntry.COLUMN_NAME_URL_ADDRESS};

        return new CursorLoader(this, mCurrentUriNews, projections
                , null
                , null
                , null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            return;
        }

        if (data.moveToFirst()) {
            int indexUrl = data.getColumnIndex(NewsContract.NewsEntry.COLUMN_NAME_URL_ADDRESS);

            String url = data.getString(indexUrl);
            mWebView.loadUrl(url);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mWebView.loadUrl("");
    }

    private class MyWebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
        super.onBackPressed();
    }
}
