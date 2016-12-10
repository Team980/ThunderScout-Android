package com.team980.thunderscout.util;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import com.team980.thunderscout.R;
import com.team980.thunderscout.match.ScoutingFlowActivity;

public class TransitionUtils {

    public static void toolbarAndStatusBarTransition(int colorFrom, int colorFromDark, int colorTo, int colorToDark, final AppCompatActivity activity) {
        // Initial colors of each system bar.
        final int statusBarColor = activity.getResources().getColor(colorFromDark);
        final int toolbarColor = activity.getResources().getColor(colorFrom);

        // Desired final colors of each bar.
        final int statusBarToColor = activity.getResources().getColor(colorToDark);
        final int toolbarToColor = activity.getResources().getColor(colorTo);

        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Use animation position to blend colors.
                float position = animation.getAnimatedFraction();

                // Apply blended color to the status bar.
                int blended = blendColors(statusBarColor, statusBarToColor, position);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity.getWindow().setStatusBarColor(blended);
                }

                // Apply blended color to the ActionBar.
                blended = blendColors(toolbarColor, toolbarToColor, position);
                ColorDrawable background = new ColorDrawable(blended);
                activity.getSupportActionBar().setBackgroundDrawable(background);

                if (activity instanceof ScoutingFlowActivity) { //we don't want a random null
                    activity.findViewById(R.id.tab_layout).setBackground(background);
                }
            }
        });

        anim.setDuration(350).start();
    }

    private static int blendColors(int from, int to, float ratio) {
        final float inverseRatio = 1f - ratio;

        final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
        final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
        final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;

        return Color.rgb((int) r, (int) g, (int) b);
    }
}
