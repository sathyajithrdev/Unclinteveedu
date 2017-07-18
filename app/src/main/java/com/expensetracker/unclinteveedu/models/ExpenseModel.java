package com.expensetracker.unclinteveedu.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sathyajith on 19/07/17.
 */

public class ExpenseModel {
    public String month;
    public boolean isClosed;
    public List<UserModel> userAmountList;
    public List<ExpenseData> expenseDataList;

    public ExpenseModel(){
        userAmountList = new ArrayList<>();
        expenseDataList = new ArrayList<>();
    }


}
