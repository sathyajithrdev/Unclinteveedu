package com.expensetracker.unclinteveedu.models;

/**
 * Created by sathyajith on 17/07/17.
 */

public class UserModel {
    public String userName;
    public String userId;
    public String name;
    public String profileImage;
    public double amount;

    public UserModel() {
    }

    public UserModel(String userName, double amount) {
        this.userName = userName;
        this.amount = amount;
    }
}
