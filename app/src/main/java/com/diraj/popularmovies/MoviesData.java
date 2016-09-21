/**
 * Created by Diraj H S on 9/13/16.
 * Copyright (c) 2016. All rights reserved.
 */
package com.diraj.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class MoviesData implements Parcelable{

    private String movieTitle;
    private String movieOverview;
    private String moviePosterPath;
    private double movieRating;
    private String movieReleaseDate;
    private String movieBackdropPath;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public MoviesData createFromParcel(Parcel in) {
            return new MoviesData(in);
        }
        @Override
        public MoviesData[] newArray(int size) {
            return new MoviesData[size];
        }
    };

    private MoviesData(Parcel in) {
        this.movieTitle = in.readString();
        this.movieOverview = in.readString();
        this.moviePosterPath = in.readString();
        this.movieReleaseDate = in.readString();
        this.movieRating = in.readDouble();
        this.movieBackdropPath = in.readString();
    }
    public MoviesData(String title, String overview, String posterPath, double rating, String releaseDate,
    String backdrop) {
        this.movieTitle = title;
        this.movieOverview = overview;
        this.moviePosterPath = posterPath;
        this.movieRating = rating;
        this.movieReleaseDate = releaseDate;
        this.movieBackdropPath = backdrop;
    }

    public String getMoviePosterPath() {
        return moviePosterPath;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public double getMovieRating() {
        return movieRating;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public String getMovieBackdropPath() {
        return movieBackdropPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(movieTitle);
        parcel.writeString(movieOverview);
        parcel.writeString(moviePosterPath);
        parcel.writeString(movieReleaseDate);
        parcel.writeDouble(movieRating);
        parcel.writeString(movieBackdropPath);
    }
}
