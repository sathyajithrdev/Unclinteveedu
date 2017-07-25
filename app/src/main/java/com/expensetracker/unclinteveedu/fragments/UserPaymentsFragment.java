package com.expensetracker.unclinteveedu.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.expensetracker.unclinteveedu.R;
import com.expensetracker.unclinteveedu.adapters.UserExpenseAdapter;
import com.expensetracker.unclinteveedu.interfaces.UserExpenseActionListener;
import com.expensetracker.unclinteveedu.managers.DatabaseManager;
import com.expensetracker.unclinteveedu.models.ExpenseData;
import com.expensetracker.unclinteveedu.models.UserModel;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserPaymentsFragment extends BaseFragment implements UserExpenseActionListener {
    private DatabaseManager databaseManager;
    private boolean mIsPaymentMode;
    private UserExpenseAdapter userExpenseAdapter;
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());

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
        View view = inflater.inflate(R.layout.fragment_user_payments, container, false);
        setProgressLayout(view, R.id.layoutProgress);
        mIsPaymentMode = getArguments().getBoolean("isPaymentMode");
        databaseManager = new DatabaseManager();
        String userId = getArguments().getString("userId");
        List<UserModel> allUserDetails = databaseManager.getPaymentDetailList((RealmResults<UserModel>) databaseManager.getAllUserDetails());
        List<ExpenseData> expenseDataList = new ArrayList<>();
        Map<String, String> allUserNames = new HashMap<>();
        if (mIsPaymentMode) {
            for (UserModel um : allUserDetails) {
                if (um.userId.equals(userId)) {
                    expenseDataList = um.paymentDetails;
                }
                allUserNames.put(um.userId, um.name);
            }
        } else {
            expenseDataList = databaseManager.getExpenseDetails(userId);
        }
        RecyclerView rvUserExpenseData = (RecyclerView) view.findViewById(R.id.rvUserExpense);
        rvUserExpenseData.setLayoutManager(new LinearLayoutManager(getActivity()));
        userExpenseAdapter = new UserExpenseAdapter(getActivity(), this, mIsPaymentMode, expenseDataList, allUserNames);
        rvUserExpenseData.setAdapter(userExpenseAdapter);
        if (expenseDataList.size() == 0) {
            TextView tvNoTransaction = (TextView) view.findViewById(R.id.tvNoTransaction);
            tvNoTransaction.setVisibility(View.VISIBLE);
            rvUserExpenseData.setVisibility(View.INVISIBLE);
            tvNoTransaction.setText(mIsPaymentMode ? getString(R.string.no_payments) : getString(R.string.no_expenses));
        }
        return view;
    }

    @Override
    public void isItemClicked(Object data) {

    }

    @Override
    public void deleteData(Object data) {

        setProgressMessage(String.format("Deleting %s data, please wait....", mIsPaymentMode ? "payment" : "expense"));
        showProgress();
        final ExpenseData dataToDelete = (ExpenseData) data;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        dataToDelete.createdDate = sdf.format(Calendar.getInstance().getTime());
        DatabaseReference archiveRef = database.getReference("expenseArchive");
        String archiveId = archiveRef.push().getKey();
        archiveRef.child(archiveId).setValue(dataToDelete, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    if (mIsPaymentMode) {
                        final DatabaseReference mUsersReference = database.getReference("users/" + dataToDelete.paidByUser);
                        mUsersReference.child("payments/" + dataToDelete.id).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                hidProgress();
                                if (databaseError == null) {
                                    databaseManager.deleteExpense(dataToDelete.id);
                                    userExpenseAdapter.expenseDeleted();
                                } else
                                    userExpenseAdapter.expenseDeleteErroredOut();
                            }
                        });
                    } else {
                        final DatabaseReference expenseRef = database.getReference("expenses/" + dataToDelete.id);
                        expenseRef.removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                hidProgress();
                                if (databaseError == null) {
                                    databaseManager.deleteExpense(dataToDelete.id);
                                    userExpenseAdapter.expenseDeleted();
                                } else
                                    userExpenseAdapter.expenseDeleteErroredOut();
                            }
                        });
                    }
                } else {
                    userExpenseAdapter.expenseDeleteErroredOut();
                    hidProgress();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseManager.onDestroy();
    }
}
