package com.expensetracker.unclinteveedu.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.expensetracker.unclinteveedu.R;
import com.expensetracker.unclinteveedu.adapters.LimitedFragmentPagerAdapter;
import com.expensetracker.unclinteveedu.fragments.UserPaymentsFragment;
import com.expensetracker.unclinteveedu.managers.DatabaseManager;
import com.expensetracker.unclinteveedu.models.UserModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailActivity extends BaseActivity implements View.OnClickListener {

    private UserModel mUserDetails;
    private DatabaseManager mDatabaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        initView();
    }

    private void initView() {
        mDatabaseManager = new DatabaseManager();
        String userId = getIntent().getStringExtra("userId");
        mUserDetails = mDatabaseManager.getUserDetails(userId);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(UserPaymentsFragment.newInstance(true, userId));
        fragmentList.add(UserPaymentsFragment.newInstance(false, userId));

        List<String> header = new ArrayList<>();
        header.add("Payments");
        header.add("Expenses");

        LimitedFragmentPagerAdapter mSectionsPagerAdapter = new LimitedFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, header);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_payment);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_add_expense);
        CircleImageView ivUser = (CircleImageView) findViewById(R.id.ivUser);
        Glide.with(this)
                .load(mUserDetails.profileImage)
                .centerCrop()
                .into(ivUser);
        ((TextView)findViewById(R.id.tvUserName)).setText(mUserDetails.name);
    }

    @Override
    public void onClick(View v) {

    }
}
