package com.example.dryunchik.news.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.dryunchik.news.fragments.SaveNewsFragment;
import com.example.dryunchik.news.fragments.SearchNewsFragment;
import com.example.dryunchik.news.fragments.TopNewsFragment;

/**
 * Created by Dryunchik on 05.04.2018.
 */

public class SelectionPagerAdapter extends FragmentStatePagerAdapter {


    public SelectionPagerAdapter(FragmentManager fm) {
        super(fm);


    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TopNewsFragment.newInstance();
            case 1:
                return SearchNewsFragment.newInstance();
            default:
                return SaveNewsFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
