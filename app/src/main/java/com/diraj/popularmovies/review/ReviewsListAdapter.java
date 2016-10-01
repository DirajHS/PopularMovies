/**
 * Created by Diraj H S on 9/25/16.
 * Copyright (c) 2016. All rights reserved.
 */

package com.diraj.popularmovies.review;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diraj.popularmovies.R;

import java.util.ArrayList;

public class ReviewsListAdapter extends RecyclerView.Adapter<ReviewsListAdapter.ViewHolder> {

    private ArrayList<ReviewsData> mReviews;

    public ReviewsListAdapter(ArrayList<ReviewsData> reviewsDatas) {
        this.mReviews = reviewsDatas;
    }

    private void startReadReviewActivity(String url, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reviews_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ReviewsData review = mReviews.get(position);

        holder.mReview = review;
        holder.author.setText(review.getAuthor());
        holder.review.setText(review.getContent());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startReadReviewActivity(holder.mReview.getUrl(), holder.view.getContext());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final View view;
        public ReviewsData mReview;
        public TextView author;
        public TextView review;
        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.author = (TextView) this.view.findViewById(R.id.author);
            this.review = (TextView) this.view.findViewById(R.id.review_content);
        }
    }
}
