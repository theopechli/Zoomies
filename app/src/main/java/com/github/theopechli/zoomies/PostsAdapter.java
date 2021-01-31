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

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<twitter4j.Status> postsList;
    private int[] smnLogos;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvScreenName;
        public TextView tvPost;
        public ImageView ivLogo;
        public ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvPost = itemView.findViewById(R.id.tvPost);
            ivLogo = itemView.findViewById(R.id.ivLogo);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }

    public PostsAdapter(Context context, List<Status> postsList, int[] smnLogos) {
        this.context = context;
        this.postsList = postsList;
        this.smnLogos = smnLogos;
    }

    @NonNull
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View listItem = inflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position) {
        holder.tvScreenName.setText(postsList.get(position).getUser().getScreenName());
        holder.tvPost.setText(postsList.get(position).getText());
        holder.ivLogo.setImageResource(smnLogos[0]);
        holder.constraintLayout.setOnClickListener(view -> {
            final Bundle bundle = new Bundle();
            bundle.putBinder("status", new ObjectWrapperForBinder(postsList.get(position)));
            Intent intent = new Intent(context, PostDetailsActivity.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }
}
