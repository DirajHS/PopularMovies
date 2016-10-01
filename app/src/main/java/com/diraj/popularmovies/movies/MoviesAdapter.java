/**
 * Created by Diraj H S on 9/13/16.
 * Copyright (c) 2016. All rights reserved.
 */
package com.diraj.popularmovies.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.diraj.popularmovies.AppConstants;
import com.diraj.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends BaseAdapter {

    private List<MoviesData> mMoviesData;
    private Context mContext;
    private GridView mGridView;

    public MoviesAdapter(Context context, List<MoviesData> moviesDataList, GridView gridView) {
        this.mContext = context;
        this.mMoviesData = moviesDataList;
        this.mGridView = gridView;
    }

    @Override
    public int getCount() {
        return mMoviesData.size();
    }

    @Override
    public Object getItem(int i) {
        return mMoviesData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ImageView imageView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.movie_layout, viewGroup, false);
        }
        imageView = (ImageView) view.findViewById(R.id.movie_poster);
        imageView.getLayoutParams().width = mGridView.getWidth() / mGridView.getNumColumns();
        String url = AppConstants.POSTER_BASE_URL + AppConstants.POSTER_WIDTH_1 +
                mMoviesData.get(i).getMoviePosterPath();
        Picasso.with(mContext).load(url).placeholder(R.drawable.poster_thumbnail).into(imageView);
        return imageView;
    }
}
