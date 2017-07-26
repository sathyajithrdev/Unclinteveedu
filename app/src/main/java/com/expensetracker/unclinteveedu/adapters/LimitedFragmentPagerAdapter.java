package com.expensetracker.unclinteveedu.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by sathy on 7/25/2017.
 * Common pager adapter to use with tabs
 */

public class LimitedFragmentPagerAdapter extends FragmentStatePagerAdapter {
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