package com.example.dryunchik.news.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.dryunchik.news.R;
import com.example.dryunchik.news.adapters.SelectionPagerAdapter;
import com.example.dryunchik.news.data.NewsDbHelper;
import com.example.dryunchik.news.preference.SettingActivity;

public class MainActivity extends AppCompatActivity {

    private SelectionPagerAdapter mSelectionPagerAdapter;

    private ViewPager mViewPager;

    private static final int PAGE_LIMIT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        mViewPager = (ViewPager) findViewById(R.id.container);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mSelectionPagerAdapter = new SelectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(PAGE_LIMIT);
        mViewPager.setAdapter(mSelectionPagerAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_setting){
            startActivity(new Intent(MainActivity.this,SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
