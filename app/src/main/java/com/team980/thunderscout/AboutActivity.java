package com.team980.thunderscout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;

public class AboutActivity extends MaterialAboutActivity {

    @Override
    protected MaterialAboutList getMaterialAboutList(Context context) {
        MaterialAboutCard.Builder titleCard = new MaterialAboutCard.Builder();

        titleCard.addItem(new MaterialAboutTitleItem.Builder()
                .text("ThunderScout")
                .icon(R.mipmap.ic_launcher)
                .build());

        titleCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Version")
                .subText(BuildConfig.VERSION_NAME)
                .icon(R.drawable.ic_info_outline_white)
                .build());

        titleCard.addItem(new MaterialAboutActionItem.Builder()
                .text("View on Google Play")
                .icon(R.drawable.ic_google_play_white)
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                        }
                    }
                })
                .build());

        titleCard.addItem(new MaterialAboutActionItem.Builder()
                .text("View on Amazon Appstore")
                .icon(R.drawable.ic_amazon_white)
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("amzn://apps/android?p=" + getPackageName())));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.amazon.com/gp/mas/dl/android?p=" + getPackageName())));
                        }
                    }
                })
                .build());

        titleCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Chief Delphi Thread")
                .icon(R.drawable.ic_forum_white)
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://www.chiefdelphi.com/forums/showthread.php?t=151953"));
                        startActivity(i);
                    }
                })
                .build());

        titleCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Fork on GitHub")
                .subText("Team980/ThunderScout-Android")
                .icon(R.drawable.ic_github_white)
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://github.com/Team980/ThunderScout-Android"));
                        startActivity(i);
                    }
                })
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
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://www.chiefdelphi.com/forums/member.php?u=91239"));
                        startActivity(i);
                    }
                })
                .build());

        MaterialAboutCard.Builder teamCard = new MaterialAboutCard.Builder();
        teamCard.addItem(new MaterialAboutTitleItem.Builder()
                .text("FRC Team 980 ThunderBots")
                .icon(R.mipmap.team980_logo)
                .build());

        teamCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Like us on Facebook")
                .subText("@Team980Thunderbots")
                .icon(R.drawable.ic_facebook_white)
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://www.facebook.com/Team980ThunderBots"));
                        startActivity(i);
                    }
                })
                .build());

        teamCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Follow us on Twitter")
                .subText("@frc980")
                .icon(R.drawable.ic_twitter_white)
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://twitter.com/frc980"));
                        startActivity(i);
                    }
                })
                .build());

        teamCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Follow us on Instagram")
                .subText("@frcteam980")
                .icon(R.drawable.ic_instagram_white)
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://www.instagram.com/frcteam980/"));
                        startActivity(i);
                    }
                })
                .build());

        teamCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Follow us on Snapchat")
                .subText("@frcteam980")
                .icon(R.drawable.ic_snapchat_white)
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://www.snapchat.com/add/frcteam980"));
                        startActivity(i);
                    }
                })
                .build());

        teamCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Subscribe to us on YouTube")
                .subText("FRC Team 980 Official")
                .icon(R.drawable.ic_youtube_white)
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://www.youtube.com/channel/UCxW_2sc2SANjckwsFJt14AQ"));
                        startActivity(i);
                    }
                })
                .build());

        teamCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Visit our website")
                .subText("team980.com")
                .icon(R.drawable.ic_web_white)
                .setOnClickListener(new MaterialAboutActionItem.OnClickListener() {
                    @Override
                    public void onClick() {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("http://team980.com/"));
                        startActivity(i);
                    }
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