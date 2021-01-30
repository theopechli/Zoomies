package com.github.theopechli.zoomies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TrendsAdapter extends RecyclerView.Adapter<TrendsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> trendsList;
    private int twitterLogo;

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

    public TrendsAdapter(Context context, ArrayList<String> trendsList, int twitterLogo) {
        this.context = context;
        this.trendsList = trendsList;
        this.twitterLogo = twitterLogo;
    }

    @NonNull
    @Override
    public TrendsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View listItem = inflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrendsAdapter.ViewHolder holder, int position) {
        holder.rowName.setText(trendsList.get(position));
        holder.rowImage.setImageResource(twitterLogo);
        holder.constraintLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, PostsActivity.class);
            intent.putExtra("com.github.theopechli.zoomies.hashtag", trendsList.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return trendsList.size();
    }
}
