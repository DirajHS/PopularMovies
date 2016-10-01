/**
 * Created by Diraj H S on 9/29/16.
 * Copyright (c) 2016. All rights reserved.
 */

package com.diraj.popularmovies.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesDBContract {

    public static final String CONTENT_AUTHORITY = "com.diraj.popularmovies";
    public static final String MOVIE_PATH = "movie";

    public static final class MovieDBEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY)
                .buildUpon().appendPath(MOVIE_PATH).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + MOVIE_PATH;

        public static final String TABLE_NAME = "movies";
        public static final String MOVIE_ID = "movie_id";
        public static final String MOVIE_TITLE = "original_title";
        public static final String MOVIE_POSTER_PATH = "poster_path";
        public static final String MOVIE_OVERVIEW = "overview";
        public static final String MOVIE_VOTE_AVERAGE = "vote_average";
        public static final String MOVIE_RELEASE_DATE = "release_date";
        public static final String MOVIE_BACKDROP_PATH = "backdrop_path";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String[] MOVIE_COLUMNS = {
                MOVIE_ID,
                MOVIE_TITLE,
                MOVIE_POSTER_PATH,
                MOVIE_OVERVIEW,
                MOVIE_VOTE_AVERAGE,
                MOVIE_RELEASE_DATE,
                MOVIE_BACKDROP_PATH
        };

        public static final int MOVIE_ID_COLUMN = 0;
        public static final int MOVIE_TITLE_COLUMN = 1;
        public static final int MOVIE_POSTER_PATH_COLUMN = 2;
        public static final int MOVIE_OVERVIEW_COLUMN = 3;
        public static final int MOVIE_VOTE_AVERAGE_COLUMN = 4;
        public static final int MOVIE_RELEASE_DATE_COLUMN = 5;
        public static final int MOVIE_BACKDROP_PATH_COLUMN = 6;
    }
}
