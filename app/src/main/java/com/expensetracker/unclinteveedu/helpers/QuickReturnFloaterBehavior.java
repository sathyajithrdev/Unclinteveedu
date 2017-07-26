package com.expensetracker.unclinteveedu.helpers;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

public class QuickReturnFloaterBehavior extends CoordinatorLayout.Behavior<View> {

    private int distance;
    private boolean isAnimating;

    public QuickReturnFloaterBehavior() {
        super();
    }

    public QuickReturnFloaterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if (isAnimating) return;
        if (dy > 0 && distance < 0 || dy < 0 && distance > 0) {
            child.animate().cancel();
            distance = 0;
        }

        distance += dy;
        final int height = child.getMeasuredHeight();

        if (height > Integer.MIN_VALUE) {
            if (distance > (height / 4) && child.getTranslationY() == 0) {
                hide(child);
            } else if (distance < 0 && child.getTranslationY() >= height) {
                show(child);
            }
        }
    }

    private void hide(final View view) {
        isAnimating = true;
        view.animate().translationY(view.getHeight()).withEndAction(new Runnable() {
            @Override
            public void run() {
                isAnimating = false;
            }
        });
    }

    private void show(View view) {
        isAnimating = true;
        view.animate().translationY(0).withEndAction(new Runnable() {
            @Override
            public void run() {
                isAnimating = false;
            }
        });
    }

}