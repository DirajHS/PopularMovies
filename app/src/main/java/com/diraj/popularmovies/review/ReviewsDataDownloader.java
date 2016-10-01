/**
 * Created by Diraj H S on 9/25/16.
 * Copyright (c) 2016. All rights reserved.
 */

package com.diraj.popularmovies.review;

import android.os.Handler;
import android.os.Looper;

import com.diraj.popularmovies.AppConstants;
import com.diraj.popularmovies.MoviesList;
import com.diraj.popularmovies.ui.SingleMovieActivity;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReviewsDataDownloader extends Thread {

    private long mId;
    private SingleMovieActivity mListener;

    public ReviewsDataDownloader(long movieId, SingleMovieActivity singleMovieActivity) {
        this.mId = movieId;
        this.mListener = singleMovieActivity;
    }

    private List<ReviewsData> getReviews() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MoviesList downloader = retrofit.create(MoviesList.class);
        Call<ReviewsList> call = downloader.getReviews(mId, AppConstants.API_KEY);
        try {
            Response<ReviewsList> response = call.execute();
            ReviewsList reviewsList = response.body();
            return reviewsList.getReviews();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public void downloadReviews() {
        final List<ReviewsData> reviewsDatas = getReviews();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                mListener.downloadedReviewsData(reviewsDatas);
            }
        });
    }

    @Override
    public void run() {
        downloadReviews();
    }
}
