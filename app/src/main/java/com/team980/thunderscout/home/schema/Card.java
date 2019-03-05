/*
 * MIT License
 *
 * Copyright (c) 2016 - 2019 Luke Myers (FRC Team 980 ThunderBots)
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
