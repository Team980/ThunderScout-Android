package com.team980.thunderscout.home.schema;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * A single card for the Feed.
 */
public class Card {

    private String title;
    private String text;
    private Drawable icon;

    private boolean dismissable;

    private ArrayList<CardAction> actions;
    private ArrayList<CardSection> sections;

    public Card(String title) {
        this.title = title;

        actions = new ArrayList<>();
        sections = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public Card setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getText() {
        return text;
    }

    public Card setText(String text) {
        this.text = text;
        return this;
    }

    public Drawable getIcon() {
        return icon;
    }

    public Card setIcon(Drawable icon) {
        this.icon = icon;
        return this;
    }

    public boolean isDismissable() {
        return dismissable;
    }

    public Card setDismissable(boolean dismissable) {
        this.dismissable = dismissable;
        return this;
    }

    public List<CardAction> getActions() {
        return actions;
    }

    public Card addAction(CardAction action) {
        actions.add(action);
        return this;
    }

    public Card removeAction(CardAction action) {
        actions.remove(action);
        return this;
    }

    public List<CardSection> getSections() {
        return sections;
    }

    public Card addSection(CardSection action) {
        sections.add(action);
        return this;
    }

    public Card removeSection(CardSection section) {
        sections.remove(section);
        return this;
    }
}
