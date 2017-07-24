package com.expensetracker.unclinteveedu.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sathyajith on 17/07/17.
 * User Detail model as per firebase design
 */

public class UserModel extends RealmObject {
    @PrimaryKey
    public String userId;
    public String userName;
    public String name;
    public String profileImage;
    public double amount;
    public RealmList<ExpenseData> paymentDetails;

    public UserModel() {
        paymentDetails = new RealmList<>();
    }

    public UserModel(String userName, double amount) {
        this.userName = userName;
        this.amount = amount;
    }
}
