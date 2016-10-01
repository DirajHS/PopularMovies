/**
 * Created by Diraj H S on 9/13/16.
 * Copyright (c) 2016. All rights reserved.
 */
package com.diraj.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.diraj.popularmovies.AnimationHandling;
import com.diraj.popularmovies.AppConstants;
import com.diraj.popularmovies.MoviesList;
import com.diraj.popularmovies.R;
import com.diraj.popularmovies.database.MoviesDBContract;
import com.diraj.popularmovies.movies.MovieDataDownloader;
import com.diraj.popularmovies.movies.MoviesAdapter;
import com.diraj.popularmovies.movies.MoviesData;
import com.diraj.popularmovies.review.ReviewsData;
import com.diraj.popularmovies.review.ReviewsList;
import com.diraj.popularmovies.trailer.TrailersData;
import com.diraj.popularmovies.trailer.TrailersList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class HomeActivity extends AppCompatActivity implements MoviesList, LoaderManager.LoaderCallbacks<Cursor> {

    private GridView mGridView;
    private List<MoviesData> mMovieList;
    private int mGridSelectedPosition;
    private boolean mToggleSort;

    private AdapterView.OnItemClickListener mGridViewClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            startNextActivity(mMovieList.get(position));
        }
    };

    private void startDownloadAndDisplayThread(boolean sortedByPopular) {
        new MovieDataDownloader(mMovieList, sortedByPopular, this).start();
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (isNetworkAvailable()) {
            updateDisplay(savedInstanceState);
        } else
            showToast(getString(R.string.network_unavailable));
    }

    private void startNextActivity(MoviesData object) {
        Intent intent = new Intent(this, SingleMovieActivity.class);
        intent.putExtra(AppConstants.INTENT_EXTRA_PARCEL, object);
        startActivity(intent);
        AnimationHandling.animateScreen(this, AnimationHandling.ANIM_TYPE.START);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimationHandling.animateScreen(this, AnimationHandling.ANIM_TYPE.CLOSE);
    }

    private void updateMenu(MenuItem menuItem) {
        if(!mToggleSort) {
            menuItem.setTitle(getString(R.string.sort));
        } else {
            menuItem.setTitle(getString(R.string.sort));
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menus, menu);
        updateMenu(menu.findItem(R.id.sort_top_rated));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ArrayList<MoviesData> moviesData = new ArrayList<>();
        data.moveToFirst();
        do {
            int id;
            String title, overview, poster, release, backdrop, ratingString;
            id = data.getInt(MoviesDBContract.MovieDBEntry.MOVIE_ID_COLUMN);
            title = data.getString(MoviesDBContract.MovieDBEntry.MOVIE_TITLE_COLUMN);
            poster = data.getString(MoviesDBContract.MovieDBEntry.MOVIE_POSTER_PATH_COLUMN);
            release = data.getString(MoviesDBContract.MovieDBEntry.MOVIE_RELEASE_DATE_COLUMN);
            backdrop = data.getString(MoviesDBContract.MovieDBEntry.MOVIE_BACKDROP_PATH_COLUMN);
            ratingString = data.getString(MoviesDBContract.MovieDBEntry.MOVIE_VOTE_AVERAGE_COLUMN);
            overview = data.getString(MoviesDBContract.MovieDBEntry.MOVIE_OVERVIEW_COLUMN);
            Double rating = Double.parseDouble(ratingString);
            MoviesData movieData = new MoviesData(id, title, overview, poster, rating, release, backdrop);
            moviesData.add(movieData);
        }while(data.moveToNext());
        mMovieList = moviesData;
        setGridViewAdapter();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                MoviesDBContract.MovieDBEntry.CONTENT_URI,
                MoviesDBContract.MovieDBEntry.MOVIE_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_top_rated:
                if (!isNetworkAvailable()) {
                    showToast(getString(R.string.network_unavailable));
                    return true;
                }
                mMovieList.clear();
                mToggleSort = !mToggleSort;
                if (mToggleSort) {
                    showToast(getString(R.string.toast_popular));
                    updateMenu(item);
                } else {
                    showToast(getString(R.string.toast_top_rated));
                    updateMenu(item);
                }
                startDownloadAndDisplayThread(mToggleSort);
                break;

            case R.id.sort_favourites:
                getSupportLoaderManager().initLoader(0, null, this);
                break;

            case R.id.refresh:
                if (isNetworkAvailable()) {
                    showToast(getString(R.string.refreshing));
                    updateDisplay(null);
                } else
                    showToast(getString(R.string.network_unavailable));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isNetworkAvailable())
            mGridSelectedPosition = mGridView.getFirstVisiblePosition();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetworkAvailable())
            mGridView.smoothScrollToPosition(mGridSelectedPosition);
        else
            showToast(getString(R.string.network_unavailable));
    }

    private void updateDisplay(Bundle savedInstance) {
        mGridView = (GridView) findViewById(R.id.gridView_Layout);
        mGridView.setOnItemClickListener(mGridViewClickListener);

        if (savedInstance != null) {
            mMovieList = savedInstance.getParcelableArrayList(AppConstants.MOVIES_LIST);
            mToggleSort = savedInstance.getBoolean(AppConstants.TOGGLE_SORT);
            mGridSelectedPosition = savedInstance.getInt(AppConstants.GRID_POSITION);
            setGridViewAdapter();
            mGridView.smoothScrollToPosition(mGridSelectedPosition);
        } else {
            mMovieList = new ArrayList<>();
            mToggleSort = true;
            startDownloadAndDisplayThread(mToggleSort);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(AppConstants.GRID_POSITION, mGridView.getFirstVisiblePosition());
        outState.putBoolean(AppConstants.TOGGLE_SORT, mToggleSort);
        ArrayList<MoviesData> moviesDatas = new ArrayList<>(mMovieList);
        outState.putParcelableArrayList(AppConstants.MOVIES_LIST, moviesDatas);
        super.onSaveInstanceState(outState);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void setGridViewAdapter() {
        mGridView.setAdapter(new MoviesAdapter(getApplicationContext(), mMovieList, mGridView));
    }

    @Override
    public void downloadedMoviesList(List<MoviesData> moviesDataArrayList) {
        mMovieList = moviesDataArrayList;
        setGridViewAdapter();
    }

    @Override
    public Call<TrailersList> getTrailers(@Path("id") long id, @Query("api_key") String apiKey) {
        return null;
    }

    @Override
    public void downloadedTrailersData(List<TrailersData> moviesTrailersArrayList) {

    }

    @Override
    public Call<ReviewsList> getReviews(@Path("id") long id, @Query("api_key") String apiKey) {
        return null;
    }

    @Override
    public void downloadedReviewsData(List<ReviewsData> moviesReviewArrayList) {

    }
}
