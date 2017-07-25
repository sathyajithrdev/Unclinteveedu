package com.expensetracker.unclinteveedu.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.expensetracker.unclinteveedu.R;
import com.expensetracker.unclinteveedu.activities.UserDetailActivity;
import com.expensetracker.unclinteveedu.adapters.UserAdapter;
import com.expensetracker.unclinteveedu.interfaces.ClickListener;
import com.expensetracker.unclinteveedu.managers.DatabaseManager;
import com.expensetracker.unclinteveedu.models.ExpenseData;
import com.expensetracker.unclinteveedu.models.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements ClickListener {

    private UserAdapter mUserAdapter;
    private List<UserModel> mUserList;
    private List<ExpenseData> mAllExpenses;
    private DatabaseManager mDatabaseManager;

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
        mUserList = new ArrayList<>();
        mAllExpenses = new ArrayList<>();
        mDatabaseManager = new DatabaseManager();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initializeView(view);

        return view;
    }

    private void initializeView(View view) {
        RecyclerView mRvUser = (RecyclerView) view.findViewById(R.id.rvUsers);
        mUserAdapter = new UserAdapter(this.getActivity(), this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new UserSpanSizeLookup());
        mRvUser.setLayoutManager(gridLayoutManager);
        mRvUser.setAdapter(mUserAdapter);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mUsersReference = database.getReference("users");
        database.getReference("expenses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalExpense = 0;
                mAllExpenses = new ArrayList<>();
                final Map<String, Double> userSpentData = new HashMap<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    mAllExpenses.add(ds.getValue(ExpenseData.class));
                    Double amount = ds.child("amount").getValue(Double.class);
                    totalExpense += amount;
                    String userId = ds.child("paidByUser").getValue(String.class);
                    Double userPaidAmount = userSpentData.get(userId);
                    userSpentData.put(userId, userPaidAmount == null ? amount : userPaidAmount + amount);
                }

                mDatabaseManager.saveExpenseDetails(mAllExpenses);

                final double finalTotalExpense = totalExpense;
                mUsersReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Map<String, Double> userReceivedAmountData = new HashMap<>();

                        mUserList = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            UserModel userData = ds.getValue(UserModel.class);
                            Double currentUserPayment = userSpentData.get(userData.userId);
                            if (currentUserPayment == null)
                                currentUserPayment = 0d;
                            DataSnapshot paymentsNode = ds.child("payments");
                            for (DataSnapshot paymentSnapShot : paymentsNode.getChildren()) {
                                ExpenseData paymentData = paymentSnapShot.getValue(ExpenseData.class);
                                if (paymentData != null) {
                                    userData.paymentDetails.add(paymentData);
                                    currentUserPayment += paymentData.amount;
                                    Double receivedAmount = userReceivedAmountData.get(paymentData.paidToUser);
                                    userReceivedAmountData.put(paymentData.paidToUser, receivedAmount == null
                                            ? paymentData.amount : receivedAmount + paymentData.amount);

                                }
                            }
                            userData.amount = (currentUserPayment == null ? 0 : currentUserPayment) - (finalTotalExpense / 5);
                            mUserList.add(userData);

                        }
                        for (UserModel u : mUserList) {
                            u.amount = u.amount - (userReceivedAmountData.get(u.userId) == null ? 0 : userReceivedAmountData.get(u.userId));
                        }
                        mUserAdapter.setUserList(mUserList);
                        mDatabaseManager.saveUserDetails(mUserList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseManager.onDestroy();
    }

    @Override
    public void isItemClicked(Object data) {
        startActivity(new Intent(getActivity(), UserDetailActivity.class).putExtra("userId", ((UserModel) data).userId));
    }

    private class UserSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        UserSpanSizeLookup() {
            super();
        }

        @Override
        public int getSpanSize(int position) {
            return (position == 0 ? 2 : 1);
        }
    }

}
