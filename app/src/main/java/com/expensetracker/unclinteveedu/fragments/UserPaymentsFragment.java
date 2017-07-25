package com.expensetracker.unclinteveedu.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.expensetracker.unclinteveedu.R;
import com.expensetracker.unclinteveedu.adapters.UserExpenseAdapter;
import com.expensetracker.unclinteveedu.interfaces.ClickListener;
import com.expensetracker.unclinteveedu.managers.DatabaseManager;
import com.expensetracker.unclinteveedu.models.UserModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserPaymentsFragment extends Fragment implements ClickListener {


    public UserPaymentsFragment() {
        // Required empty public constructor
    }

    public static UserPaymentsFragment newInstance(boolean isPaymentMode, String userId) {

        Bundle args = new Bundle();
        args.putBoolean("isPaymentMode", isPaymentMode);
        args.putString("userId", userId);
        UserPaymentsFragment fragment = new UserPaymentsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        boolean isPaymentMode = getArguments().getBoolean("isPaymentMode");
        String userId = getArguments().getString("userId");
        View view = inflater.inflate(R.layout.fragment_user_payments, container, false);
        DatabaseManager databaseManager = new DatabaseManager();
        UserModel userData = databaseManager.getUserDetails(userId);
        RecyclerView rvUserExpenseData = (RecyclerView) view.findViewById(R.id.rvUserExpense);
        rvUserExpenseData.setLayoutManager(new LinearLayoutManager(getActivity()));
        Log.e("ExpenseCount", String.valueOf(userData.paymentDetails.size()));
        rvUserExpenseData.setAdapter(new UserExpenseAdapter(getActivity(), this, isPaymentMode, userData.paymentDetails));
        return view;
    }

    @Override
    public void isItemClicked(Object data) {

    }
}
