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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends BaseFragment implements UserExpenseActionListener {

    private DatabaseManager databaseManager;
    private UserExpenseAdapter userExpenseAdapter;
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());


    public static HistoryFragment newInstance() {

        Bundle args = new Bundle();

        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        setProgressLayout(view, R.id.layoutProgress);
        databaseManager = new DatabaseManager();
        List<ExpenseData> expenseDataList = databaseManager.getAllExpenseDetails();
        Map<String, String> allUserNames = new HashMap<>();
        for (UserModel um : databaseManager.getAllUserDetails()) {
            allUserNames.put(um.userId, um.name);
        }
        RecyclerView rvUserExpenseData = (RecyclerView) view.findViewById(R.id.rvAllExpense);
        rvUserExpenseData.setLayoutManager(new LinearLayoutManager(getActivity()));
        userExpenseAdapter = new UserExpenseAdapter(getActivity(), this, false, expenseDataList, allUserNames);
        rvUserExpenseData.setAdapter(userExpenseAdapter);
        if (expenseDataList.size() == 0) {
            TextView tvNoTransaction = (TextView) view.findViewById(R.id.tvNoTransaction);
            tvNoTransaction.setVisibility(View.VISIBLE);
            rvUserExpenseData.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    @Override
    public void isItemClicked(Object data) {

    }

    @Override
    public void deleteData(Object data) {
        setProgressMessage("Deleting expense data, please wait....");
        showProgress();
        final ExpenseData dataToDelete = (ExpenseData) data;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference archiveRef = database.getReference("expenseArchive");
        String archiveId = archiveRef.push().getKey();
        archiveRef.child(archiveId).setValue(dataToDelete, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {

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
