package com.expensetracker.unclinteveedu.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.expensetracker.unclinteveedu.R;
import com.expensetracker.unclinteveedu.helpers.AlertHelper;

import org.w3c.dom.Text;

/**
 * Created by sathyajith on 20/07/17.
 * Base activity for the application
 */

public class BaseActivity<T> extends AppCompatActivity {

    private AlertHelper mAlertHelper;
    private View mProgressLayout;
    private TextView mTvProgressMessage;

    SharedPreferences mPreference;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreference = getApplicationContext().getSharedPreferences("com.expensetracker.unclinteveedu.UnclinteVeedu", Context.MODE_PRIVATE); // 0 - for private mode
        mEditor = mPreference.edit();
    }

    protected T getPreference(String key, Class<T> valueType) {
        if (valueType == String.class)
            return (T) mPreference.getString(key, "");
        if (valueType == Boolean.class)
            return (T) ((Boolean) mPreference.getBoolean(key, false));
        return null;
    }

    protected void setPreference(String key, T value) {
        if (value instanceof String) {
            mEditor.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            mEditor.putBoolean(key, (Boolean) value);
        }

    }

    protected void showAlert(String message, boolean isCancellable) {
        if (mAlertHelper == null)
            mAlertHelper = new AlertHelper(this);
        mAlertHelper.showAlert(getString(R.string.app_name), message, isCancellable);
    }

    protected void setLoggedInPreference(boolean value) {
        mEditor.putBoolean("isLoggedIn", value).commit();
    }

    protected void setLoggedInUserId(String userId) {

        mEditor.putString("loggedInUserId", userId).commit();
    }

    protected String getLoggedInUserId() {
        return mPreference.getString("loggedInUserId", "");
    }


    protected void setProgressLayout(int layoutProgress) {
        mProgressLayout = findViewById(layoutProgress);
        if (mProgressLayout != null)
            mTvProgressMessage = (TextView) mProgressLayout.findViewById(R.id.tvProgressMessage);
    }

    protected void showProgress() {
        if (mProgressLayout != null)
            mProgressLayout.setVisibility(View.VISIBLE);
    }


    protected void hidProgress() {
        if (mProgressLayout != null)
            mProgressLayout.setVisibility(View.GONE);
    }

    protected void setProgressMessage(String message) {
        if (mTvProgressMessage != null)
            mTvProgressMessage.setText(message);
    }

}
