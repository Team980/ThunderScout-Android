package com.team980.thunderscout.info.statistics;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team980.thunderscout.R;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<String> commentsList;

    public CommentsAdapter(List<String> l) {
        commentsList = l;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View dataView = inflater.inflate(R.layout.comment_view, parent, false);

        return new CommentViewHolder(dataView);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int i) {
        String comment = commentsList.get(i);

        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public void add(String s) {
        commentsList.add(s);
        notifyItemInserted(commentsList.size() - 1);
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private TextView key;

        public CommentViewHolder(View itemView) {
            super(itemView);

            key = (TextView) itemView.findViewById(R.id.comment);
        }

        public void bind(String defText) {
            key.setText(defText);
        }
    }
}
