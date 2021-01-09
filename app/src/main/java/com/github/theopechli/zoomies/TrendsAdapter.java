package com.github.theopechli.zoomies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TrendsAdapter extends RecyclerView.Adapter<TrendsAdapter.ViewHolder> {

    Context context;
    ArrayList<String> trendsList;
    int[] smnLogos;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rowName;
        ImageView rowImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowName = itemView.findViewById(R.id.tvTrends);
            rowImage = itemView.findViewById(R.id.ivLogo);
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
        View view = inflater.inflate(R.layout.single_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrendsAdapter.ViewHolder holder, int position) {
        holder.rowName.setText(trendsList.get(position));
        holder.rowImage.setImageResource(smnLogos[0]);
    }

    @Override
    public int getItemCount() {
        return trendsList.size();
    }
}
