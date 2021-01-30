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

public class PostRepliesAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Status> postRepliesList;
    private int[] smnLogos;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView rowName;
        public ImageView rowImage;
        public ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowName = itemView.findViewById(R.id.tvPost);
            rowImage = itemView.findViewById(R.id.ivLogo);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }

    public PostRepliesAdapter(Context context, List<Status> postRepliesList, int[] smnLogos) {
        this.context = context;
        this.postRepliesList = postRepliesList;
        this.smnLogos = smnLogos;
    }

    @NonNull
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View listItem = inflater.inflate(R.layout.list_item, parent, false);
        PostsAdapter.ViewHolder viewHolder = new PostsAdapter.ViewHolder(listItem);

        return viewHolder;
    }

    public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position) {
        holder.rowName.setText(postRepliesList.get(position).getText());
        holder.rowImage.setImageResource(smnLogos[0]);
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
