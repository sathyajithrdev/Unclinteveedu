package com.expensetracker.unclinteveedu.interfaces;

/**
 * Created by sathyajith on 25/07/17.
 * Interface to communicate delete action between two classes
 */

public interface UserExpenseActionListener {
    void isItemClicked(Object data);

    void deleteData(Object data);
}
