package com.example.dryunchik.news.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.dryunchik.news.preference.NewsPreference;
import com.example.dryunchik.news.preference.SettingActivity;
import com.example.dryunchik.news.webservice.App;

import java.util.ArrayList;
import java.util.Arrays;

public class TopNewsFragment extends Fragment implements RecyclerViewClickListener,SharedPreferences.OnSharedPreferenceChangeListener {
    private ArrayList<NewsData> mList;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter mAdapter;
    private ProgressBar mProgressBar;

   private boolean PREFERENCES_HAVE_BEEN_UPDATED = false;
   

    public static final String LOG_TAG = TopNewsFragment.class.getSimpleName();

    public TopNewsFragment() {
    }

   public static TopNewsFragment newInstance(){
        return new TopNewsFragment();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_top_news, container
                , false);

        mProgressBar = (ProgressBar)view.findViewById(R.id.progress_bar);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mList = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyRecyclerAdapter(mList, getActivity(),this);
        mRecyclerView.setAdapter(mAdapter);
        String choose = NewsPreference.setPreference(getActivity());
        httpRequest(choose);

        // Picasso.get().setIndicatorsEnabled(true);

        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .registerOnSharedPreferenceChangeListener(this);

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
//        MenuItem item = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) item.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                mList.clear();
//                httpRequest(query);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });

        MenuItem menuItemSearch = menu.findItem(R.id.action_search);
        menuItemSearch.setVisible(false);

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
                String query = NewsPreference.setPreference(getActivity());
                httpRequest(query);
                return true;
            case android.R.id.home:
                return true;
//            case R.id.action_setting:
//                startActivity(new Intent(getActivity(),SettingActivity.class));
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void httpRequest(String query) {


        retrofit2.Call<JSONResponse> call = App.getApi().getTopData(query);

        call.enqueue(new retrofit2.Callback<JSONResponse>() {

            @Override
            public void onResponse(retrofit2.Call<JSONResponse> call, retrofit2.Response<JSONResponse> response) {
                if (response.isSuccessful()){
                   mProgressBar.setVisibility(View.INVISIBLE);
                    JSONResponse jsonResponse = response.body();
                    mList.addAll(Arrays.asList(jsonResponse.getArticles()));
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<JSONResponse> call, Throwable t) {
            }
        });
    }


    @Override
    public void recyclerViewClickListener(NewsData data) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(NewsContract.NewsEntry.COLUMN_NAME_TITLE,data.getTitle());
        contentValues.put(NewsContract.NewsEntry.COLUMN_NAME_DESCRIPTION,data.getDescription());
        contentValues.put(NewsContract.NewsEntry.COLUMN_NAME_URL_ADDRESS,data.getUrl());

        getContext().getContentResolver().insert(NewsContract.NewsEntry.CONTENT_URI,contentValues);



        Snackbar.make(getView(),R.string.news_save, BaseTransientBottomBar.LENGTH_SHORT).show();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (PREFERENCES_HAVE_BEEN_UPDATED){
            String choose = NewsPreference.setPreference(getActivity());
            mList.clear();
            mAdapter.notifyDataSetChanged();
            httpRequest(choose);
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PREFERENCES_HAVE_BEEN_UPDATED = true;
    }
}




