package com.ddmax.zjnucloud.ui.widget.fab;


import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.jorgecastilloprz.FABProgressCircle;


/**
 * @author ddMax
 * @since 2016/2/19 21:56.
 */
public class FABAppBarBehavior extends CoordinatorLayout.Behavior<RelativeLayout> {
    public static final String TAG = FABAppBarBehavior.class.getSimpleName();

    private static final boolean SNACKBAR_BEHAVIOR_ENABLED = Build.VERSION.SDK_INT >= 11;
    private Rect mTmpRect;

    public FABAppBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RelativeLayout child, View dependency) {
        return SNACKBAR_BEHAVIOR_ENABLED && dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, RelativeLayout child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            return updateFabVisibility(parent, (AppBarLayout) dependency, child);
        }
        return false;
    }

    private boolean updateFabVisibility(CoordinatorLayout parent,
            AppBarLayout appBarLayout, RelativeLayout child) {
        final CoordinatorLayout.LayoutParams lp =
                (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (lp.getAnchorId() != appBarLayout.getId()) {
            return false;
        }

        // First, let's get the visible rect of the dependency
        if (mTmpRect == null) {
            mTmpRect = new Rect();
        }

        final Rect rect = mTmpRect;
        ViewGroupUtils.getDescendantRect(parent, appBarLayout, rect);

        // We'll use 2/3 height of AppBarLayout
        final int appBarHeight = (int) (appBarLayout.getHeight() / 1.5);
        // Get the FAB
        FABProgressCircle fabProgressCircle = (FABProgressCircle) child.getChildAt(0);
        FloatingActionButton fabWrapped = (FloatingActionButton) fabProgressCircle.getChildAt(0);
        if (fabWrapped != null) {
            if (rect.bottom < appBarHeight) {
                fabWrapped.hide();
                fabProgressCircle.setVisibility(View.INVISIBLE);
            } else {
                fabWrapped.show();
                fabProgressCircle.setVisibility(View.VISIBLE);
            }
        }
        return true;
    }

}
