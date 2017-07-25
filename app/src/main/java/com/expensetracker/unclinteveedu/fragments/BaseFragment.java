package com.expensetracker.unclinteveedu.fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.expensetracker.unclinteveedu.R;

/**
 * Created by sathyajith on 26/07/17.
 */

public class BaseFragment extends Fragment {

    private View mProgressLayout;
    private TextView mTvProgressMessage;

    protected void setProgressLayout(View view, int layoutProgress) {
        mProgressLayout = view.findViewById(layoutProgress);
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
