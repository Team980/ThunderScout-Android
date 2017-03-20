/*
 * MIT License
 *
 * Copyright (c) 2016 - 2017 Luke Myers (FRC Team 980 ThunderBots)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.team980.thunderscout.util;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.team980.thunderscout.R;
import com.team980.thunderscout.match.ScoutingFlowActivity;

public class TransitionUtils {

    //TODO add fromDefault?
    public static void toolbarAndStatusBarTransitionFromResources(int colorFrom, int colorFromDark, int colorTo, int colorToDark, AppCompatActivity activity) {
        // Initial colors of each system bar.
        final int statusBarColor = activity.getResources().getColor(colorFromDark);
        final int toolbarColor = activity.getResources().getColor(colorFrom);

        // Desired final colors of each bar.
        final int statusBarToColor = activity.getResources().getColor(colorToDark);
        final int toolbarToColor = activity.getResources().getColor(colorTo);

        toolbarAndStatusBarTransition(toolbarColor, statusBarColor, toolbarToColor, statusBarToColor, activity);
    }

    public static void toolbarAndStatusBarTransition(final int toolbarColor, final int statusBarColor, final int toolbarToColor, final int statusBarToColor, final AppCompatActivity activity) {
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
                activity.findViewById(R.id.toolbar).setBackgroundColor(blended);

                if (activity.findViewById(R.id.tab_layout) != null) { //we don't want a random NPE
                    activity.findViewById(R.id.tab_layout).setBackgroundColor(blended);
                }
            }
        });
        anim.setDuration(350).start();

        //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) { //tint in Overview
        //ActivityManager.TaskDescription tDesc = new ActivityManager.TaskDescription(null, null, toolbarToColor);
        //activity.setTaskDescription(tDesc);
        //}
    }

    public static void toolbarTransition(final int toolbarColor, final int toolbarToColor, final Toolbar toolbar) {
        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Use animation position to blend colors.
                float position = animation.getAnimatedFraction();

                // Apply blended color to the ActionBar.
                int blended = blendColors(toolbarColor, toolbarToColor, position);
                ColorDrawable background = new ColorDrawable(blended);
                toolbar.setBackgroundDrawable(background);
            }
        });
        anim.setDuration(350).start();

        //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) { //tint in Overview
        //ActivityManager.TaskDescription tDesc = new ActivityManager.TaskDescription(null, null, toolbarToColor);
        //activity.setTaskDescription(tDesc);
        //}
    }

    private static int blendColors(int from, int to, float ratio) {
        final float inverseRatio = 1f - ratio;

        final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
        final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
        final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;

        return Color.rgb((int) r, (int) g, (int) b);
    }
}
