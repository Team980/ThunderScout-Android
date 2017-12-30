package com.team980.thunderscout.home.schema;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * A custom inflatable layout that can be added to the inside of a card.
 */
public class CardSection {

    @LayoutRes
    private int layout;
    private SectionCallback callback;

    public CardSection(@LayoutRes int layout, @Nullable SectionCallback callback) {
        this.layout = layout;
        this.callback = callback;
    }

    public int getLayoutId() {
        return layout;
    }

    public void bind(View sectionView) {
        if (callback != null) {
            callback.bind(sectionView);
        }
    }

    /**
     * A callback to bind the custom layout.
     */
    public interface SectionCallback {
        void bind(View sectionView);
    }
}
