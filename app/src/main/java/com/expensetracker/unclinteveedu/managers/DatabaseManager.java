package com.expensetracker.unclinteveedu.managers;

import com.expensetracker.unclinteveedu.models.UserModel;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;

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

    public void onDestroy() {
        if (mRealm != null && !mRealm.isClosed()) {
            mRealm.close();
        }
    }

    public List<UserModel> getAllUserDetails() {
        RealmQuery<UserModel> query = mRealm.where(UserModel.class);
        return query.findAll();
    }
}
