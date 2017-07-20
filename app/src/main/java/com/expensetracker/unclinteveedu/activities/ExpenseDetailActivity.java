package com.expensetracker.unclinteveedu.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.expensetracker.unclinteveedu.R;
import com.expensetracker.unclinteveedu.adapters.PayeeAdapter;
import com.expensetracker.unclinteveedu.managers.DatabaseManager;
import com.expensetracker.unclinteveedu.models.ExpenseData;
import com.expensetracker.unclinteveedu.models.UserModel;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ExpenseDetailActivity extends BaseActivity implements View.OnClickListener {

    private Calendar selectedDate = Calendar.getInstance();
    private EditText mEtPaymentDate;
    private DatabaseManager mDatabaseManager;
    private PayeeAdapter mPayeeAdapter;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);
        getSupportActionBar().setTitle("Add Expense");
        initClickListeners();
        initComponents();

    }

    private void initComponents() {
        mDatabaseManager = new DatabaseManager();
        List<UserModel> mUserList = mDatabaseManager.getAllUserDetails();
        RecyclerView rvPayee = (RecyclerView) findViewById(R.id.rvUsers);
        rvPayee.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL));
        mEtPaymentDate = (EditText) findViewById(R.id.etDate);
        mPayeeAdapter = new PayeeAdapter(this);
        rvPayee.setAdapter(mPayeeAdapter);
        mPayeeAdapter.setUserList(mUserList);
    }

    private void initClickListeners() {
        findViewById(R.id.etDate).setOnClickListener(this);
        findViewById(R.id.btnAddOrUpdate).setOnClickListener(this);
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            selectedDate.set(Calendar.YEAR, year);
            selectedDate.set(Calendar.MONTH, monthOfYear);
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updatePaymentDate();
        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etDate: {
                new DatePickerDialog(ExpenseDetailActivity.this, date, selectedDate
                        .get(Calendar.YEAR), selectedDate.get(Calendar.MONTH),
                        selectedDate.get(Calendar.DAY_OF_MONTH)).show();
                break;
            }
            case R.id.btnAddOrUpdate: {
                addPayment();
                break;
            }
        }
    }

    private void updatePaymentDate() {
        mEtPaymentDate.setText(sdf.format(selectedDate.getTime()));
    }

    private void addPayment() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference expenseRef = database.getReference("expenses");
        String expenseId = expenseRef.push().getKey();
        ExpenseData expenseData = new ExpenseData();
        expenseData.id = expenseId;

        expenseData.expenseName = getEtText(R.id.etExpenditure);
        expenseData.amount = Double.parseDouble(getEtText(R.id.etAmount));
        expenseData.paidByUser = mPayeeAdapter.getSelectedUserId();
        expenseData.paymentDate = getEtText(R.id.etDate);
        expenseData.createdDate = sdf.format(Calendar.getInstance().getTime());
        expenseData.createdUser = getLoggedInUserId();
        expenseRef.child(expenseId).setValue(expenseData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Toast.makeText(ExpenseDetailActivity.this, R.string.info_success_payment, Toast.LENGTH_LONG).show();
                    supportFinishAfterTransition();
                } else {
                    Toast.makeText(ExpenseDetailActivity.this, R.string.error_network, Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private String getEtText(int et) {
        return ((EditText) findViewById(et)).getText().toString();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseManager.onDestroy();
    }
}
