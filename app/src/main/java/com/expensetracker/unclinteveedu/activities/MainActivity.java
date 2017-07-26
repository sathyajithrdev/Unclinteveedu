package com.expensetracker.unclinteveedu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.expensetracker.unclinteveedu.R;
import com.expensetracker.unclinteveedu.adapters.LimitedFragmentPagerAdapter;
import com.expensetracker.unclinteveedu.fragments.HistoryFragment;
import com.expensetracker.unclinteveedu.fragments.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        initView();

    }

    private void initView() {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(HomeFragment.newInstance());
        fragmentList.add(HistoryFragment.newInstance());

        List<String> header = new ArrayList<>();
        header.add("Home");
        header.add("History");

        LimitedFragmentPagerAdapter mSectionsPagerAdapter = new LimitedFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, header);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_history);
        findViewById(R.id.ivAddPayment).setOnClickListener(this);
        findViewById(R.id.ivAddExpense).setOnClickListener(this);
        findViewById(R.id.ivSettings).setOnClickListener(this);
        findViewById(R.id.ivLogout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivAddExpense: {
                startActivity(new Intent(MainActivity.this, ExpenseDetailActivity.class));
                break;
            }
            case R.id.ivAddPayment: {
                startActivity(new Intent(MainActivity.this, PaymentActivity.class));
                break;
            }
            case R.id.ivLogout: {
                setIsUserLoggedIn(false);
                setLoggedInUserId("");
                startActivity(new Intent(this, LoginActivity.class));
                supportFinishAfterTransition();
            }

        }
    }

}
