package com.expensetracker.unclinteveedu.interfaces;

import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;

/**
 * Created by sathy on 7/26/2017.
 */

public interface AnimateViewHolder {

    void preAnimateAddImpl(final RecyclerView.ViewHolder holder);

    void preAnimateRemoveImpl(final RecyclerView.ViewHolder holder);

    void animateAddImpl(final RecyclerView.ViewHolder holder, ViewPropertyAnimatorListener listener);

    void animateRemoveImpl(final RecyclerView.ViewHolder holder,
                           ViewPropertyAnimatorListener listener);
}