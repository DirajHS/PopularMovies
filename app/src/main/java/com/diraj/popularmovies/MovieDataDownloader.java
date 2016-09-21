/**
 * Created by Diraj H S on 9/13/16.
 * Copyright (c) 2016. All rights reserved.
 */
package com.diraj.popularmovies;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

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

    MovieDataDownloader(List<MoviesData> movieList, boolean getPopular, HomeActivity activity) {
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
            Log.e(AppConstants.AppName, "release date was not successfully parsed");
        }
        return date;
    }
    private void parseJSON(String results) throws Exception {
        JSONObject jsonObject;
        jsonObject = new JSONObject(results);
        JSONArray jsonArray = jsonObject.getJSONArray(AppConstants.JSON_root);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject movieData = jsonArray.getJSONObject(i);
            String title = movieData.getString(AppConstants.JSON_original_title);
            String overview = movieData.getString(AppConstants.JSON_overview);
            String posterPath = movieData.getString(AppConstants.JSON_poster_path);
            double rating = movieData.getDouble(AppConstants.JSON_vote_average);
            String releaseDate = movieData.getString(AppConstants.JSON_release_date);
            releaseDate = getEasilyReadableDate(releaseDate);
            String backdrop = movieData.getString(AppConstants.JSON_backdrop_path);
            mMoviesList.add(new MoviesData(title, overview, posterPath, rating, releaseDate, backdrop));
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
                url = new URL(AppConstants.PopularMoviesURL);
            } else {
                url = new URL(AppConstants.RatedMoviesURL);
            }
            movieDbConnection = (HttpURLConnection) url.openConnection();
            inputStream = movieDbConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            results = bufferedReader.readLine();
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
