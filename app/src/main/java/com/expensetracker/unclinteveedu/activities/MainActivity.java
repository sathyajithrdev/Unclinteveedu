package com.expensetracker.unclinteveedu.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.expensetracker.unclinteveedu.R;
import com.expensetracker.unclinteveedu.models.ExpenseData;
import com.expensetracker.unclinteveedu.models.ExpenseModel;
import com.expensetracker.unclinteveedu.models.UserModel;
import com.expensetracker.unclinteveedu.fragments.HistoryFragment;
import com.expensetracker.unclinteveedu.fragments.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    float dX;
    float dY;
    int lastAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_LONG).show();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference expenseRef = database.getReference("expenses");
                final Query mUsersReference = expenseRef.orderByChild("month").equalTo("2017-08");

                mUsersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e("TestFirebase", String.valueOf(dataSnapshot == null));
                        if (dataSnapshot == null || dataSnapshot.getValue() == null) {
                            DatabaseReference dr = expenseRef.push();

                            List<UserModel> userAmountData = new ArrayList<UserModel>();
                            userAmountData.add(new UserModel("aswin", 1000));
                            userAmountData.add(new UserModel("jophy", 2000));
                            ExpenseModel expenseModel = new ExpenseModel();
                            expenseModel.month = "2017-08";
                            expenseModel.userAmountList = userAmountData;
                            ExpenseData e = new ExpenseData();
                            e.amount = 10000;
                            e.expenseName = "Rent";
                            expenseModel.expenseDataList.add(e);
                            dr.setValue(expenseModel);

                            Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_LONG).show();
                        } else {
                            Log.e("nonnull", dataSnapshot.getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

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

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                view.setY(event.getRawY() + dY);
                view.setX(event.getRawX() + dX);
                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN) {
                    Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_LONG).show();
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference mUsersReference = database.getReference("expenses");
                    mUsersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.hasChild("2017-08")) {
                                DatabaseReference dr = mUsersReference.push();
                                dr.setValue("2017-08", new UserModel("aswin", 1000));
                                List<UserModel> userAmountData = new ArrayList<UserModel>();
                                userAmountData.add(new UserModel("aswin", 1000));
                                userAmountData.add(new UserModel("jophy", 2000));

                                Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                break;

            default:
                return false;
        }
        return true;
    }
}
