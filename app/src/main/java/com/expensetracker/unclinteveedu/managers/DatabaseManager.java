package com.expensetracker.unclinteveedu.managers;

import com.expensetracker.unclinteveedu.models.ExpenseData;
import com.expensetracker.unclinteveedu.models.UserModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by sathyajith on 20/07/17.
 * Database manager class for the application
 */

public class DatabaseManager {
    private Realm mRealm;

    public DatabaseManager() {
        mRealm = Realm.getDefaultInstance();
    }

    public void saveUserDetails(final List<UserModel> userModelList) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(userModelList);
            }
        });
    }

    public List<UserModel> getAllUserDetails() {
        RealmQuery<UserModel> query = mRealm.where(UserModel.class);
        return query.findAll();
    }

    public void saveExpenseDetails(final List<ExpenseData> expenseList) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(expenseList);
            }
        });
    }

    public List<ExpenseData> getAllExpenseDetails() {
        RealmQuery<ExpenseData> query = mRealm.where(ExpenseData.class);
        RealmResults<ExpenseData> allData = query.findAll();
        return mRealm.copyFromRealm(allData);
    }

    public void onDestroy() {
        if (mRealm != null && !mRealm.isClosed()) {
            mRealm.close();
        }
    }

    public UserModel getUserDetails(String userId) {
        RealmQuery<UserModel> query = mRealm.where(UserModel.class).equalTo("userId", userId);
        return query.findFirst();
    }

    public List<ExpenseData> getExpenseDetails(String userId) {
        RealmQuery<ExpenseData> query = mRealm.where(ExpenseData.class).equalTo("paidByUser", userId).equalTo("isPayment", false);
        RealmResults<ExpenseData> allData = query.findAll();
        return mRealm.copyFromRealm(allData);
    }

    public List<UserModel> getPaymentDetailList(RealmResults<UserModel> allUserDetails) {
        return mRealm.copyFromRealm(allUserDetails);
    }

    public void deleteExpense(final String id) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(ExpenseData.class).equalTo("id", id).findAll().deleteAllFromRealm();
            }
        });
    }

    public void deleteAllExpense() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(ExpenseData.class).findAll().deleteAllFromRealm();
            }
        });
    }
}
