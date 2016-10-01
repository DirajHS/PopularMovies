/**
 * Created by Diraj H S on 9/29/16.
 * Copyright (c) 2016. All rights reserved.
 */

package com.diraj.popularmovies.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.Nullable;

public class MoviesDBProvider extends ContentProvider {

    private static UriMatcher sUriMatcher = buildUriMatcher();
    static final int CODE = 100;
    private MoviesDBCreator mMoviesDB;
    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MoviesDBContract.CONTENT_AUTHORITY, MoviesDBContract.MOVIE_PATH, CODE);
        return  uriMatcher;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case CODE :
                return MoviesDBContract.MovieDBEntry.CONTENT_TYPE;
            default: throw new UnsupportedOperationException();
        }
    }

    @Override
    public boolean onCreate() {
        mMoviesDB = new MoviesDBCreator(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor resultCursor;
        switch (sUriMatcher.match(uri)) {
            case CODE:
                resultCursor = mMoviesDB.getReadableDatabase().query(MoviesDBContract.MovieDBEntry.TABLE_NAME,
                        strings, s, strings1, null, null, s1);
                break;
            default:
                throw new UnsupportedOperationException();
        }
        if(getContext() != null) {
            resultCursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return resultCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case CODE:
                long res = mMoviesDB.getWritableDatabase().insert(MoviesDBContract.MovieDBEntry.TABLE_NAME,
                        null, contentValues);
                if(res > 0) {
                    returnUri = MoviesDBContract.MovieDBEntry.buildMovieUri(res);
                } else {
                    throw new SQLException("Failed to insert row: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException();
        }
        if(getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        if(null == s) {
            s = "1";
        }
        int deletedRows;
        switch (sUriMatcher.match(uri)) {
            case CODE:
                deletedRows = mMoviesDB.getWritableDatabase().delete(MoviesDBContract.MovieDBEntry.TABLE_NAME,
                        s, strings);
                break;
            default:
                throw new UnsupportedOperationException();
        }
        if(deletedRows != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int updatedRows;
        switch (sUriMatcher.match(uri)) {
            case CODE:
                updatedRows = mMoviesDB.getWritableDatabase().update(MoviesDBContract.MovieDBEntry.TABLE_NAME,
                        contentValues, s, strings);
                break;
            default:
                throw new UnsupportedOperationException();
        }
        if(updatedRows != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updatedRows;
    }
}
