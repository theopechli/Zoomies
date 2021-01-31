package com.github.theopechli.zoomies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import twitter4j.Status;

public class PostRepliesAdapter extends RecyclerView.Adapter<PostRepliesAdapter.ViewHolder> {

    private Context context;
    private Status postParent;
    private List<Status> postRepliesList;
    private int[] smnLogos;
    private View listItem;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvScreenName2;
        public TextView tvScreenNameReply;
        public TextView tvPost2;
        public TextView tvPostReply;
        public ImageView ivLogo2;
        public ImageView ivLogoReply;
        public ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvScreenName2 = itemView.findViewById(R.id.tvScreenName2);
            tvScreenNameReply = itemView.findViewById(R.id.tvScreenNameReply);
            tvPost2 = itemView.findViewById(R.id.tvPost2);
            tvPostReply = itemView.findViewById(R.id.tvPostReply);
            ivLogo2 = itemView.findViewById(R.id.ivLogo2);
            ivLogoReply = itemView.findViewById(R.id.ivLogoReply);
            constraintLayout = itemView.findViewById(R.id.constraintLayout2);
        }
    }

    public PostRepliesAdapter(Context context, Status postParent, List<Status> postRepliesList, int[] smnLogos) {
        this.context = context;
        this.postParent = postParent;
        this.postRepliesList = postRepliesList;
        this.smnLogos = smnLogos;
    }

    @NonNull
    @Override
    public PostRepliesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        listItem = inflater.inflate(R.layout.list_item_2, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    public void onBindViewHolder(@NonNull PostRepliesAdapter.ViewHolder holder, int position) {
        holder.tvScreenName2.setText(postParent.getUser().getScreenName());
        holder.tvScreenNameReply.setText(postRepliesList.get(position).getUser().getScreenName());
        holder.tvPost2.setText(postParent.getText());
        holder.tvPost2.append("\n\n" + postParent.getCreatedAt());
        holder.tvPost2.append("\n\nLikes: " + postParent.getFavoriteCount());
        holder.tvPost2.append("\t\tRetweets: " + postParent.getRetweetCount());
        holder.tvPostReply.setText(postRepliesList.get(position).getText());
        holder.tvPost2.append("\n\n" + postRepliesList.get(position).getCreatedAt());
        holder.tvPostReply.append("\n\nLikes: " + postRepliesList.get(position).getFavoriteCount());
        holder.tvPostReply.append("\t\tRetweets: " + postRepliesList.get(position).getRetweetCount());
        holder.ivLogo2.setImageResource(smnLogos[0]);
        holder.ivLogoReply.setImageResource(smnLogos[0]);
        holder.constraintLayout.setOnClickListener(view -> {
            final Bundle bundle = new Bundle();
            bundle.putBinder("status", new ObjectWrapperForBinder(postRepliesList.get(position)));
            Intent intent = new Intent(context, PostDetailsActivity.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });

        final Button btnReplyToPost = listItem.findViewById(R.id.btnReplyToPost);
        btnReplyToPost.setOnClickListener(v -> {
            final Bundle bundle = new Bundle();
            bundle.putBinder("status", new ObjectWrapperForBinder(postParent));
            Intent intent = new Intent(context, CreatePostActivity.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });

        final Button btnReplyToPostReply = listItem.findViewById(R.id.btnReplyToPostReply);
        btnReplyToPostReply.setOnClickListener(v -> {
            final Bundle bundle = new Bundle();
            bundle.putBinder("status", new ObjectWrapperForBinder(postRepliesList.get(position)));
            Intent intent = new Intent(context, CreatePostActivity.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });

        final Button btnLikePost = listItem.findViewById(R.id.btnLikePost);
        LikePost(postParent, btnLikePost);

        final Button btnRetweetPost = listItem.findViewById(R.id.btnRetweetPost);
        RetweetPost(postParent, btnRetweetPost);

        final Button btnLikePostReply = listItem.findViewById(R.id.btnLikePostReply);
        LikePost(postRepliesList.get(position), btnLikePostReply);

        final Button btnRetweetPostReply = listItem.findViewById(R.id.btnRetweetPostReply);
        RetweetPost(postRepliesList.get(position), btnRetweetPostReply);


    }


    public void LikePost(twitter4j.Status status, Button button) {
        if (status.isFavorited()) {
            button.setText("Unlike");
        } else {
            button.setText("Like");
        }

        button.setOnClickListener(v -> {
            if (button.getText().equals("Unlike")) {
                button.setText("Like");
                new PostDetailsActivity.UnLikePostTask().execute(status);
            } else {
                button.setText("Unlike");
                new PostDetailsActivity.LikePostTask().execute(status);
            }
        });
    }

    public static void RetweetPost(twitter4j.Status status, Button button) {
        if (status.isRetweetedByMe()) {
            button.setText("Unretweet");
        } else {
            button.setText("Retweet");
        }

        button.setOnClickListener(v -> {
            if (button.getText().equals("Unretweet")) {
                button.setText("Retweet");
                new PostDetailsActivity.UnRetweetPostTask().execute(status);
            } else {
                button.setText("Unretweet");
                new PostDetailsActivity.RetweetPostTask().execute(status);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postRepliesList.size();
    }
}
