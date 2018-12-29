package com.example.dryunchik.news.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.dryunchik.news.R;
import com.example.dryunchik.news.data.NewsContract;

/**
 * Created by Dryunchik on 02.05.2018.
 */

public class MyCursorAdapter extends CursorAdapter {


    public MyCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_database,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv_title = (TextView)view.findViewById(R.id.tv_title_name);
        TextView tv_descriptions = (TextView)view.findViewById(R.id.tv_description_name);

        String title = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_NAME_TITLE));
        String descriptions = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_NAME_DESCRIPTION));

        tv_title.setText(title);
        tv_descriptions.setText(descriptions);

    }
}
