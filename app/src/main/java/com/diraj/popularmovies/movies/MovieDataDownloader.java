/**
 * Created by Diraj H S on 9/13/16.
 * Copyright (c) 2016. All rights reserved.
 */
package com.diraj.popularmovies.movies;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.diraj.popularmovies.AppConstants;
import com.diraj.popularmovies.ui.HomeActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MovieDataDownloader extends Thread {

    private List<MoviesData> mMoviesList;
    private boolean mGetPopular;
    private HomeActivity mListener;

    public MovieDataDownloader(List<MoviesData> movieList, boolean getPopular, HomeActivity activity) {
        this.mMoviesList = movieList;
        this.mGetPopular = getPopular;
        this.mListener = activity;
    }

    private String getEasilyReadableDate(String date) {
        final String inputDatePattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputDatePattern, Locale.UK);
        try {
            Date formattedDate = inputFormat.parse(date);
            return DateFormat.getDateInstance().format(formattedDate);
        } catch(ParseException exception) {
            Log.e(AppConstants.APP_NAME, "release date was not successfully parsed");
        }
        return date;
    }
    private void parseJSON(String results) throws Exception {
        JSONObject jsonObject;
        jsonObject = new JSONObject(results);
        JSONArray jsonArray = jsonObject.getJSONArray(AppConstants.JSON_ROOT);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject movieData = jsonArray.getJSONObject(i);
            int id = movieData.getInt(AppConstants.JSON_ID);
            String title = movieData.getString(AppConstants.JSON_ORIGINAL_TITLE);
            String overview = movieData.getString(AppConstants.JSON_OVERVIEW);
            String posterPath = movieData.getString(AppConstants.JSON_POSTER_PATH);
            double rating = movieData.getDouble(AppConstants.JSON_VOTE_AVERAGE);
            String releaseDate = movieData.getString(AppConstants.JSON_RELEASE_DATE);
            releaseDate = getEasilyReadableDate(releaseDate);
            String backdrop = movieData.getString(AppConstants.JSON_BACKDROP_PATH);
            mMoviesList.add(new MoviesData(id, title, overview, posterPath, rating, releaseDate, backdrop));
        }
    }

    private void downloadComplete() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                mListener.downloadedMoviesList(mMoviesList);
            }
        });
    }

    private void getMovieData() {
        URL url;
        HttpURLConnection movieDbConnection;
        InputStream inputStream;
        BufferedReader bufferedReader;
        String results;
        try {
            if (this.mGetPopular) {
                url = new URL(AppConstants.POPULAR_MOVIES_URL);
            } else {
                url = new URL(AppConstants.RATED_MOVIES_URL);
            }
            movieDbConnection = (HttpURLConnection) url.openConnection();
            inputStream = movieDbConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            results = bufferedReader.readLine();
            Log.d(AppConstants.APP_NAME, results);
            parseJSON(results);

            downloadComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        getMovieData();
    }
}
