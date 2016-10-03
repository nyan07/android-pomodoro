package com.ladyboomerang.pomodoro.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

public class TabsPagerAdapter extends FragmentStatePagerAdapter
{
    private String[] tabTitles;
    private SparseArray<Fragment> fragments;

    public TabsPagerAdapter(FragmentManager fm, String[] tabTitles, SparseArray<Fragment> fragments)
    {
        super(fm);
        this.tabTitles = tabTitles;
        this.fragments = fragments;
    }

    @Override
    public int getCount()
    {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position)
    {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return this.tabTitles[position];
    }
}
