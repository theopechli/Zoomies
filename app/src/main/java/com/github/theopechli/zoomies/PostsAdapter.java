package com.github.theopechli.zoomies;

import android.content.Context;
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
        public TextView rowName;
        public ImageView rowImage;
        public ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowName = itemView.findViewById(R.id.tvTrends);
            rowImage = itemView.findViewById(R.id.ivLogo);
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
        holder.rowName.setText(postsList.get(position).getText().toString());
        holder.rowImage.setImageResource(smnLogos[0]);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }
}
