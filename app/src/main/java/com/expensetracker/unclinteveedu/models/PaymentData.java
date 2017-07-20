package com.expensetracker.unclinteveedu.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sathyajith on 20/07/17.
 */

public class PaymentData extends RealmObject {
    @PrimaryKey
    public String id;
    public String payeeUserId;
    public double amount;
    public String createByUserId;
    public String cratedDate;
    public String imagePath;

}
