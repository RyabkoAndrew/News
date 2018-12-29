package com.example.dryunchik.news.fragments;


import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dryunchik.news.R;
import com.example.dryunchik.news.adapters.MyCursorAdapter;
import com.example.dryunchik.news.data.NewsContract;
import com.example.dryunchik.news.webservice.WebActivity;


public class SaveNewsFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    private MyCursorAdapter mCursorAdapter;
    private ListView mListView;

    public static final int URL_LOADER = 0;



    public SaveNewsFragment() {
        // Required empty public constructor
    }

    public static SaveNewsFragment newInstance(){
        return new SaveNewsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_save_news,container,false);


        mListView = (ListView)view.findViewById(R.id.listView);
        mListView.setEmptyView(view.findViewById(R.id.tv_empty_view));

        mCursorAdapter = new MyCursorAdapter(getActivity(),null);
        mListView.setAdapter(mCursorAdapter);



        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),WebActivity.class);
                Uri currentNewsUri = ContentUris.withAppendedId(NewsContract.NewsEntry.CONTENT_URI,id);
                intent.setData(currentNewsUri);
                startActivity(intent);
            }
        });

        deleteNews();

        getActivity().getSupportLoaderManager().initLoader(URL_LOADER,null,this);

        return view;
    }

    @NonNull
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {NewsContract.NewsEntry._ID,
                NewsContract.NewsEntry.COLUMN_NAME_TITLE,
                NewsContract.NewsEntry.COLUMN_NAME_DESCRIPTION,
                NewsContract.NewsEntry.COLUMN_NAME_URL_ADDRESS};

        return new android.support.v4.content.CursorLoader(getActivity(),
                NewsContract.NewsEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<Cursor> loader, Cursor data) {

        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         int id = item.getItemId();

         if (id == R.id.action_delete_all){
             deleteAllNews();
             return true;
         }else {
             return super.onOptionsItemSelected(item);
         }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem itemSearch = menu.findItem(R.id.action_search);
        itemSearch.setVisible(false);

        MenuItem itemRefresh = menu.findItem(R.id.action_refresh);
        itemRefresh.setVisible(false);

    }

    private void deleteNews(){
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.delete_news);
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null){
                            dialog.dismiss();
                        }
                    }
                });
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().getContentResolver()
                                .delete(ContentUris.withAppendedId(NewsContract.NewsEntry.CONTENT_URI,id)
                                        ,null
                                        ,null);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });
    }

    private void deleteAllNews(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_all_news);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               if (dialog != null){
                   dialog.dismiss();
               }
            }
        });
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().getContentResolver()
                        .delete(NewsContract.NewsEntry.CONTENT_URI,null,null);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
