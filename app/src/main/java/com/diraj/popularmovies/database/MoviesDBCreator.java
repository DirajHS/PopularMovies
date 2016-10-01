/**
 * Created by Diraj H S on 9/29/16.
 * Copyright (c) 2016. All rights reserved.
 */

package com.diraj.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MoviesDBCreator extends SQLiteOpenHelper {

    private static final String MOVIE_DATABASE_NAME = "movies.db";
    private static final int MOVIES_DATABASE_VERSION = 1;

    public MoviesDBCreator(Context context) {
        super(context, MOVIE_DATABASE_NAME, null, MOVIES_DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_QUERY = "CREATE TABLE " + MoviesDBContract.MovieDBEntry.TABLE_NAME +
                "(" + MoviesDBContract.MovieDBEntry._ID + " INTEGER PRIMARY KEY, " +
                MoviesDBContract.MovieDBEntry.MOVIE_ID + " INTEGER NOT NULL, " +
                MoviesDBContract.MovieDBEntry.MOVIE_TITLE + " TEXT NOT NULL, " +
                MoviesDBContract.MovieDBEntry.MOVIE_POSTER_PATH + " TEXT NOT NULL," +
                MoviesDBContract.MovieDBEntry.MOVIE_OVERVIEW + " TEXT NOT NULL," +
                MoviesDBContract.MovieDBEntry.MOVIE_VOTE_AVERAGE + " TEXT NOT NULL," +
                MoviesDBContract.MovieDBEntry.MOVIE_RELEASE_DATE + " TEXT NOT NULL," +
                MoviesDBContract.MovieDBEntry.MOVIE_BACKDROP_PATH + " TEXT NOT NULL " +
                ");";
        sqLiteDatabase.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesDBContract.MovieDBEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
