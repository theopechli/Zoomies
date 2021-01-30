package com.github.theopechli.zoomies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        View listItem = inflater.inflate(R.layout.list_item_2, parent, false);
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
    }

    @Override
    public int getItemCount() {
        return postRepliesList.size();
    }
}
