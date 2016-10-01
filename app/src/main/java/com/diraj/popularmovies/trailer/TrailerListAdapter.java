/**
 * Created by Diraj H S on 9/23/16.
 * Copyright (c) 2016. All rights reserved.
 */
package com.diraj.popularmovies.trailer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.diraj.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.ViewHolder> {

    private final ArrayList<TrailersData> mTrailers;

    public TrailerListAdapter(ArrayList<TrailersData> trailersData) {
        this.mTrailers = trailersData;
    }

    private void startVideoActivity(TrailersData trailersData, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailersData.getTrailerUrl()));
        context.startActivity(intent);
    }

    @Override
    public TrailerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailers_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrailerListAdapter.ViewHolder holder, int position) {

        final TrailersData trailer = mTrailers.get(position);
        Context context = holder.mView.getContext();
        holder.mTrailer = trailer;

        float paddingLeft = 0;
        if (position == 0) {
            paddingLeft = context.getResources().getDimension(R.dimen.trailer_padding);
        }

        float paddingRight = 0;
        if (position + 1 != getItemCount()) {
            paddingRight = context.getResources().getDimension(R.dimen.trailer_padding) / 2;
        }

        holder.mView.setPadding((int) paddingLeft, 0, (int) paddingRight, 0);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVideoActivity(trailer, holder.mView.getContext());
            }
        });
        String thumbnailUrl = "http://img.youtube.com/vi/" + trailer.getKey() + "/0.jpg";
        Picasso.with(holder.mView.getContext())
                .load(thumbnailUrl)
                .config(Bitmap.Config.RGB_565)
                .into(holder.mThumbnail);

    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public TrailersData mTrailer;
        public ImageView mThumbnail;
        public ViewHolder(View view) {
            super(view);
            this.mView = view;
            this.mThumbnail = (ImageView) view.findViewById(R.id.trailer_thumbnail);
        }
    }
}
