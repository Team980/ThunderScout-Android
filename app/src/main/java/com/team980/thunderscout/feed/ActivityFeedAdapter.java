package com.team980.thunderscout.feed;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.team980.thunderscout.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class ActivityFeedAdapter extends ExpandableRecyclerAdapter<ActivityFeedAdapter.FeedEntryViewHolder, ActivityFeedAdapter.OperationViewHolder> {

    private ArrayList<FeedEntry> feedEntries;

    public ActivityFeedAdapter(ArrayList<FeedEntry> parentItemList) {
        super(parentItemList);

        feedEntries = parentItemList;
    }

    // onCreate ...
    @Override
    public FeedEntryViewHolder onCreateParentViewHolder(ViewGroup parent) {
        View entryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_entry, parent, false);
        return new FeedEntryViewHolder(entryView);
    }

    @Override
    public OperationViewHolder onCreateChildViewHolder(ViewGroup child) {
        View operationView = LayoutInflater.from(child.getContext()).inflate(R.layout.feed_operation, child, false);
        return new OperationViewHolder(operationView);
    }

    // onBind ...
    @Override
    public void onBindParentViewHolder(FeedEntryViewHolder holder, int position, ParentListItem parentListItem) {
        FeedEntry entry = (FeedEntry) parentListItem;
        holder.bind(entry);
    }

    @Override
    public void onBindChildViewHolder(OperationViewHolder holder, int position, Object childListItem) {
        EntryOperationWrapper wrapper = (EntryOperationWrapper) childListItem;
        holder.bind(wrapper);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public void addFeedEntry(FeedEntry entryToAdd) {
        feedEntries.add(entryToAdd);
        notifyParentItemInserted(feedEntries.lastIndexOf(entryToAdd)); //This makes sense

        Collections.sort(feedEntries); //sort by date
    }

    /**
     * Removes all the entries from the list.
     * Called when we delete things.
     */
    public void clearEntries() {
        if (feedEntries.size() == 0) {
            //list is empty
            return;
        }

        notifyParentItemRangeRemoved(0, feedEntries.size());
        getParentItemList().removeAll(feedEntries);
    }

    public class FeedEntryViewHolder extends ParentViewHolder {

        private ImageView icon;
        private TextView type;
        private TextView timestamp;

        private ImageButton expandButton;

        public FeedEntryViewHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.entry_icon);
            type = (TextView) itemView.findViewById(R.id.entry_type);
            timestamp = (TextView) itemView.findViewById(R.id.entry_timestamp);

            expandButton = (ImageButton) itemView.findViewById(R.id.entry_expandButton);
        }

        public void bind(FeedEntry entry) {
            icon.setImageResource(entry.getType().getIcon());
            type.setText(entry.getType().toString());
            timestamp.setText(SimpleDateFormat.getDateTimeInstance().format(entry.getTimestamp()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isExpanded()) {
                        collapseView();
                        expandButton.setImageResource(R.drawable.ic_expand_more_white_24dp);
                    } else {
                        expandView();
                        expandButton.setImageResource(R.drawable.ic_expand_less_white_24dp);
                    }
                }
            });

            expandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isExpanded()) {
                        collapseView();
                        expandButton.setImageResource(R.drawable.ic_expand_more_white_24dp);
                    } else {
                        expandView();
                        expandButton.setImageResource(R.drawable.ic_expand_less_white_24dp);
                    }
                }
            });

            if (entry.containsFailure()) {
                itemView.setBackgroundColor(itemView.getResources().getColor(R.color.error));
            }
        }
    }

    public class OperationViewHolder extends ChildViewHolder {

        private ImageView icon;
        private TextView operationType;
        private TextView operationStatus;

        public OperationViewHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.operation_icon);
            operationType = (TextView) itemView.findViewById(R.id.operation_type);
            operationStatus = (TextView) itemView.findViewById(R.id.operation_status);
        }

        public void bind(EntryOperationWrapper wrapper) {
            icon.setImageResource(wrapper.getType().getIcon());
            operationType.setText(wrapper.getType().toString());
            operationStatus.setText(wrapper.getStatus().toString());

            if (wrapper.getStatus() != EntryOperationWrapper.EntryOperationStatus.OPERATION_SUCCESSFUL) {
                icon.setColorFilter(new PorterDuffColorFilter(itemView.getResources().getColor(R.color.error), PorterDuff.Mode.MULTIPLY));
            }
        }
    }
}
