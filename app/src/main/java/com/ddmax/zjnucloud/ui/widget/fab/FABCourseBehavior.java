package com.ddmax.zjnucloud.ui.widget.fab;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;

import com.github.jorgecastilloprz.FABProgressCircle;

/**
 * @author ddMax
 * @since 2016/2/19 21:56.
 */
public class FABCourseBehavior extends CoordinatorLayout.Behavior<FABProgressCircle> {
    public static final String TAG = FABCourseBehavior.class.getSimpleName();

    private static final boolean SNACKBAR_BEHAVIOR_ENABLED = Build.VERSION.SDK_INT >= 11;

    public FABCourseBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FABProgressCircle child, View dependency) {
        return SNACKBAR_BEHAVIOR_ENABLED && dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FABProgressCircle child, View dependency) {
        updateFabTranslationForSnackbar(parent, child, dependency);
        return false;
    }

    private boolean updateFabTranslationForSnackbar(CoordinatorLayout parent,
            final FABProgressCircle fab, View snackbar) {
        float translationY = Math.min(0, snackbar.getTranslationY() - snackbar.getHeight());
        fab.setTranslationY(translationY);
        return false;
    }

}
