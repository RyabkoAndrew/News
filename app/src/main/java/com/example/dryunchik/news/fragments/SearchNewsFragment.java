package com.example.dryunchik.news.fragments;


import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.dryunchik.news.R;
import com.example.dryunchik.news.RecyclerViewClickListener;
import com.example.dryunchik.news.adapters.MyRecyclerAdapter;
import com.example.dryunchik.news.data.NewsContract;
import com.example.dryunchik.news.model.JSONResponse;
import com.example.dryunchik.news.model.NewsData;
import com.example.dryunchik.news.webservice.App;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Callback;
import retrofit2.Response;

public class SearchNewsFragment extends Fragment implements RecyclerViewClickListener {

    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter mAdapter;
    private ArrayList<NewsData> mList;
    private ProgressBar mProgressBar;

    private static final String DEFAULT_SEARCH = "Україна";
    private String mCurrentSearch;


    public SearchNewsFragment() {
        // Required empty public constructor
    }

    public static SearchNewsFragment newInstance() {
        return new SearchNewsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_news, container, false);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mList = new ArrayList<>();

        mAdapter = new MyRecyclerAdapter(mList, getActivity(), this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        httpRequest(DEFAULT_SEARCH);


        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mList.clear();
                mAdapter.notifyDataSetChanged();
                mCurrentSearch = query;
                httpRequest(mCurrentSearch);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItem menuItemDelete = menu.findItem(R.id.action_delete_all);
        menuItemDelete.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh:
                mList.clear();
                mAdapter.notifyDataSetChanged();
                if (mCurrentSearch == null){
                    httpRequest(DEFAULT_SEARCH);
                }else {
                    httpRequest(mCurrentSearch);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void httpRequest(String query) {
        retrofit2.Call<JSONResponse> call = App.getApi().getData(query);
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(retrofit2.Call<JSONResponse> call, Response<JSONResponse> response) {
                mProgressBar.setVisibility(View.INVISIBLE);
                JSONResponse jsonResponse = response.body();
                mList.addAll(Arrays.asList(jsonResponse.getArticles()));
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(retrofit2.Call<JSONResponse> call, Throwable t) {
            }
        });
    }

    @Override
    public void recyclerViewClickListener(NewsData data) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(NewsContract.NewsEntry.COLUMN_NAME_TITLE, data.getTitle());
        contentValues.put(NewsContract.NewsEntry.COLUMN_NAME_DESCRIPTION, data.getDescription());
        contentValues.put(NewsContract.NewsEntry.COLUMN_NAME_URL_ADDRESS, data.getUrl());

        getContext().getContentResolver().insert(NewsContract.NewsEntry.CONTENT_URI, contentValues);

        Snackbar.make(getView(), R.string.news_save, BaseTransientBottomBar.LENGTH_SHORT).show();
    }
}



