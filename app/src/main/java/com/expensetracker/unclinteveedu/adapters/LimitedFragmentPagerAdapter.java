package com.expensetracker.unclinteveedu.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.expensetracker.unclinteveedu.fragments.HistoryFragment;
import com.expensetracker.unclinteveedu.fragments.HomeFragment;

import java.util.List;

/**
 * Created by sathy on 7/25/2017.
 */

public class LimitedFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;
    private List<String> mHeaders;

    public LimitedFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> headers) {
        super(fm);
        mFragmentList = fragmentList;
        mHeaders = headers;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return mFragmentList.get(0);
            case 1:
                return mFragmentList.get(1);
            default:
                return mFragmentList.get(0);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mHeaders.get(0);
            case 1:
                return mHeaders.get(1);
        }
        return mHeaders.get(0);
    }
}