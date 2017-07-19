package com.expensetracker.unclinteveedu.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.expensetracker.unclinteveedu.R;
import com.expensetracker.unclinteveedu.fragments.HistoryFragment;
import com.expensetracker.unclinteveedu.fragments.HomeFragment;
import com.expensetracker.unclinteveedu.models.ExpenseData;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        initView();


//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_LONG).show();
//                final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                final DatabaseReference expenseRef = database.getReference("expenses");
//                final Query mUsersReference = expenseRef.orderByChild("month").equalTo("2017-08");
//
//                mUsersReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Log.e("TestFirebase", String.valueOf(dataSnapshot == null));
//                        if (dataSnapshot == null || dataSnapshot.getValue() == null) {
//                            DatabaseReference dr = expenseRef.push();
//
//                            List<UserModel> userAmountData = new ArrayList<UserModel>();
//                            userAmountData.add(new UserModel("aswin", 1000));
//                            userAmountData.add(new UserModel("jophy", 2000));
//                            ExpenseFirebaseModel expenseModel = new ExpenseFirebaseModel();
//                            expenseModel.month = "2017-08";
//                            expenseModel.userAmountList = userAmountData;
//                            ExpenseData e = new ExpenseData();
//                            e.amount = 10000;
//                            e.expenseName = "Rent";
//
//                            dr.setValue(expenseModel);
//
//                            Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_LONG).show();
//                        } else {
//                            ExpenseData e = new ExpenseData();
//                            e.amount = 10000;
//                            e.expenseName = "Rent";
//                            for (DataSnapshot d : dataSnapshot.getChildren()) {
//                                ExpenseFirebaseModel dd = d.getValue(ExpenseFirebaseModel.class);
//                                Log.e("TestFirebase", dd.expenseDataList.size() + "");
//
//                                expenseRef.child(d.getKey()).child("expenseDataList").push().setValue(e);
//
//                                Log.e("TestFirebase", d.getKey());
//                                break;
//                            }
//
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        });

//        fab.setOnTouchListener(this);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_LONG).show();
//                final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                final DatabaseReference mUsersReference = database.getReference("expenses");
//                mUsersReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (!dataSnapshot.hasChild("2017-08")) {
//                            mUsersReference.setValue("2017-08");
//                            Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        });

    }

    private void initView() {
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_history);
        findViewById(R.id.ivAddPayment).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivAddPayment: {
                addPayment();
            }
        }
    }

    private void addPayment() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference expenseRef = database.getReference("expenses");
        String expenseId = expenseRef.push().getKey();
        ExpenseData expenseData = new ExpenseData();
        expenseData.id = expenseId;
        expenseData.expenseName = "Cooking Items";
        expenseData.amount = 700;
        expenseData.paidByUser = "0";
        expenseData.paymentDate = "18-07-2017";
        expenseData.createdDate = "19-07-2017";
        expenseData.createdUser = "0";
        expenseRef.child(expenseId).setValue(expenseData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Toast.makeText(MainActivity.this, "Expense added successfully", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return HomeFragment.newInstance();
                case 1:
                    return HistoryFragment.newInstance();
                default:
                    return HomeFragment.newInstance();
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
                    return "Home";
                case 1:
                    return "History";
            }
            return null;
        }
    }

}
