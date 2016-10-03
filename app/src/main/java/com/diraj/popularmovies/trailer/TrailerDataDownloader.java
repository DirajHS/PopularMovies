/**
 * Created by Diraj H S on 9/23/16.
 * Copyright (c) 2016. All rights reserved.
 */

package com.diraj.popularmovies.trailer;

import android.os.Handler;
import android.os.Looper;

import com.diraj.popularmovies.AppConstants;
import com.diraj.popularmovies.MoviesList;
import com.diraj.popularmovies.ui.SingleMovieFragment;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrailerDataDownloader extends Thread{

    private SingleMovieFragment mListner;
    private long mID;
    public TrailerDataDownloader(long id, SingleMovieFragment singleMovieFragment) {
        this.mID = id;
        this.mListner = singleMovieFragment;
    }

    private List<TrailersData> getTrailers() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MoviesList downloader = retrofit.create(MoviesList.class);
        Call<TrailersList> call = downloader.getTrailers(mID, AppConstants.API_KEY);
        try {
            Response<TrailersList> response = call.execute();
            TrailersList trailersList = response.body();
            return trailersList.getTrailers();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public void downloadTrailers() {
        final List<TrailersData> trailersData = getTrailers();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                mListner.downloadedTrailersData(trailersData);
            }
        });
    }

    @Override
    public void run() {
        downloadTrailers();
    }
}
