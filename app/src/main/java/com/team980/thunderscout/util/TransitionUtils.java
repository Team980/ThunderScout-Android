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
import android.app.ActivityManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;

import com.team980.thunderscout.R;

import static android.content.Context.ACTIVITY_SERVICE;

public class TransitionUtils {


    public static void toolbarAndStatusBarTransition(@ColorInt int toolbarColor, @ColorInt int statusBarColor, @ColorInt int toolbarToColor, @ColorInt int statusBarToColor, AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager activityManager = (ActivityManager) activity.getSystemService(ACTIVITY_SERVICE);
            ActivityManager.TaskDescription current = activityManager.getAppTasks().get(0).getTaskInfo().taskDescription;
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(current.getLabel(),
                    current.getIcon(), toolbarToColor);
            activity.setTaskDescription(taskDesc);
        }

        toolbarFadeTransition(toolbarColor, statusBarColor, toolbarToColor, statusBarToColor, activity);
    }

    public static void toolbarAndStatusBarTransitionFromResources(@ColorRes int colorFrom, @ColorRes int colorFromDark, @ColorRes int colorTo, @ColorRes int colorToDark, AppCompatActivity activity) {
        // Initial colors of each system bar.
        final int statusBarColor = activity.getResources().getColor(colorFromDark);
        final int toolbarColor = activity.getResources().getColor(colorFrom);

        // Desired final colors of each bar.
        final int statusBarToColor = activity.getResources().getColor(colorToDark);
        final int toolbarToColor = activity.getResources().getColor(colorTo);

        toolbarAndStatusBarTransition(toolbarColor, statusBarColor, toolbarToColor, statusBarToColor, activity);
    }

    private static void toolbarFadeTransition(@ColorInt final int toolbarColor, @ColorInt final int statusBarColor, @ColorInt final int toolbarToColor, @ColorInt final int statusBarToColor, final AppCompatActivity activity) {
        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(animation -> {
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
        });
        anim.setDuration(350).start();
    }

    private static int blendColors(@ColorInt int from, @ColorInt int to, float ratio) {
        final float inverseRatio = 1f - ratio;

        final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
        final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
        final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;

        return Color.rgb((int) r, (int) g, (int) b);
    }
}
