package com.expensetracker.unclinteveedu.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by sathyajith on 20/07/17.
 * Base activity for the application
 */

public class BaseActivity<T> extends AppCompatActivity {

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

    protected void setLoggedInPreference(boolean value) {
        mEditor.putBoolean("isLoggedIn", value).commit();
    }

    protected void setLoggedInUserId(String userId) {

        mEditor.putString("loggedInUserId", userId).commit();
    }

    protected String getLoggedInUserId() {
        return mPreference.getString("loggedInUserId", "");
    }

}
