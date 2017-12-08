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

package com.team980.thunderscout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

public class AboutActivity extends MaterialAboutActivity {

    @Override
    protected MaterialAboutList getMaterialAboutList(Context context) {
        MaterialAboutCard.Builder titleCard = new MaterialAboutCard.Builder();

        titleCard.addItem(new MaterialAboutTitleItem.Builder()
                .text("ThunderScout")
                .icon(R.mipmap.ic_launcher)
                .build());

        titleCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Version " + BuildConfig.VERSION_NAME)
                .subText("Click for patch notes")
                .icon(R.drawable.ic_info_outline_white)
                .setOnClickAction(() -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
                    builder.setTitle("New in version " + BuildConfig.VERSION_NAME);
                    builder.setMessage(R.string.update_notes);
                    builder.setPositiveButton("OK", null);
                    builder.create().show();
                })
                .build());

        titleCard.addItem(new MaterialAboutActionItem.Builder()
                .text("View on Google Play")
                .icon(R.drawable.ic_google_play_white)
                .setOnClickAction(() -> {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                })
                .build());

        titleCard.addItem(new MaterialAboutActionItem.Builder()
                .text("View on Amazon Appstore")
                .icon(R.drawable.ic_amazon_white)
                .setOnClickAction(() -> {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("amzn://apps/android?p=" + getPackageName())));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.amazon.com/gp/mas/dl/android?p=" + getPackageName())));
                    }
                })
                .build());

        titleCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Fork on GitHub")
                .subText("Team980/ThunderScout-Android")
                .icon(R.drawable.ic_github_white)
                .setOnClickAction(() -> {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://github.com/Team980/ThunderScout-Android"));
                    startActivity(i);
                })
                .build());

        /*titleCard.addItem(new MaterialAboutActionItem.Builder()
                .text("View on Chief Delphi")
                .icon(R.drawable.ic_forum_white)
                .setOnClickAction(() -> {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://www.chiefdelphi.com/forums/showthread.php?t=151953"));
                    startActivity(i);
                })
                .build());*/ //TODO no 2018 thread

        titleCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Open source licenses")
                .icon(R.drawable.ic_code_white_24dp)
                .setOnClickAction(() -> new LibsBuilder()
                        .withActivityStyle(Libs.ActivityStyle.DARK)
                        .withActivityTheme(R.style.ThunderScout_LibraryInfo)
                        .withActivityTitle("Open source licenses")
                        .withFields(R.string.class.getFields())
                        .start(this))
                .build());

        MaterialAboutCard.Builder authorCard = new MaterialAboutCard.Builder();
        authorCard.title("Author");

        authorCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Luke Myers")
                .subText("Lead Developer")
                .icon(R.drawable.ic_person_white_24dp)
                .build());

        authorCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Chief Delphi")
                .subText("@19lmyers")
                .icon(R.drawable.ic_forum_white)
                .setOnClickAction(() -> {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://www.chiefdelphi.com/forums/member.php?u=91239"));
                    startActivity(i);
                })
                .build());

        MaterialAboutCard.Builder teamCard = new MaterialAboutCard.Builder();
        teamCard.addItem(new MaterialAboutTitleItem.Builder()
                .text("FRC Team 980 ThunderBots")
                .icon(R.mipmap.team980_logo) //TODO standard team icon?
                .build());

        teamCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Like us on Facebook")
                .subText("@Team980Thunderbots")
                .icon(R.drawable.ic_facebook_white)
                .setOnClickAction(() -> {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://www.facebook.com/Team980ThunderBots"));
                    startActivity(i);
                })
                .build());

        teamCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Follow us on Twitter")
                .subText("@frc980")
                .icon(R.drawable.ic_twitter_white)
                .setOnClickAction(() -> {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://twitter.com/frc980"));
                    startActivity(i);
                })
                .build());

        teamCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Follow us on Instagram")
                .subText("@frcteam980")
                .icon(R.drawable.ic_instagram_white)
                .setOnClickAction(() -> {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://www.instagram.com/frcteam980/"));
                    startActivity(i);
                })
                .build());

        teamCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Follow us on Snapchat")
                .subText("@frcteam980")
                .icon(R.drawable.ic_snapchat_white)
                .setOnClickAction(() -> {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://www.snapchat.com/add/frcteam980"));
                    startActivity(i);
                })
                .build());

        teamCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Subscribe to us on YouTube")
                .subText("FRC Team 980 Official")
                .icon(R.drawable.ic_youtube_white)
                .setOnClickAction(() -> {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://www.youtube.com/channel/UCxW_2sc2SANjckwsFJt14AQ"));
                    startActivity(i);
                })
                .build());

        teamCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Visit our website")
                .subText("team980.com")
                .icon(R.drawable.ic_web_white)
                .setOnClickAction(() -> {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("http://team980.com/"));
                    startActivity(i);
                })
                .build());

        return new MaterialAboutList.Builder()
                .addCard(titleCard.build())
                .addCard(authorCard.build())
                .addCard(teamCard.build())
                .build();
    }

    @Override
    protected CharSequence getActivityTitle() {
        return getString(R.string.mal_title_about);
    }

}