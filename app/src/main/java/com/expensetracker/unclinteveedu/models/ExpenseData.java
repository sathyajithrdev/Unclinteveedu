package com.expensetracker.unclinteveedu.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sathyajith on 19/07/17.
 */

public class ExpenseData extends RealmObject {
    @PrimaryKey
    public String id;
    public String expenseName;
    public boolean isPayment;
    public double amount;
    public String paymentDate;
    public String paidByUser;
    public String paidToUser;
    public String createdDate;
    public String createdUser;
    public String imageUrl;
    public boolean isClosed;
}
