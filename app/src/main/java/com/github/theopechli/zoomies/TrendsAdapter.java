package com.github.theopechli.zoomies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TrendsAdapter extends RecyclerView.Adapter<TrendsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> trendsList;
    private int[] smnLogos;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView rowName;
        public ImageView rowImage;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowName = itemView.findViewById(R.id.tvTrends);
            rowImage = itemView.findViewById(R.id.ivLogo);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }

    public TrendsAdapter(Context context, ArrayList<String> trendsList, int[] smnLogos) {
        this.context = context;
        this.trendsList = trendsList;
        this.smnLogos = smnLogos;
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
        holder.rowImage.setImageResource(smnLogos[0]);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostsActivity.class);
                intent.putExtra("com.github.theopechli.zoomies.Hashtag", trendsList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trendsList.size();
    }
}
