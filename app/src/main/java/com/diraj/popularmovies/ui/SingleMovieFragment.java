/**
 * Created by Diraj H S on 9/13/16.
 * Copyright (c) 2016. All rights reserved.
 */
package com.diraj.popularmovies.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.diraj.popularmovies.AppConstants;
import com.diraj.popularmovies.MoviesList;
import com.diraj.popularmovies.R;
import com.diraj.popularmovies.database.MoviesDBContract;
import com.diraj.popularmovies.movies.MoviesData;
import com.diraj.popularmovies.review.ReviewsData;
import com.diraj.popularmovies.review.ReviewsDataDownloader;
import com.diraj.popularmovies.review.ReviewsList;
import com.diraj.popularmovies.review.ReviewsListAdapter;
import com.diraj.popularmovies.trailer.TrailerDataDownloader;
import com.diraj.popularmovies.trailer.TrailerListAdapter;
import com.diraj.popularmovies.trailer.TrailersData;
import com.diraj.popularmovies.trailer.TrailersList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class SingleMovieFragment extends android.support.v4.app.Fragment implements MoviesList {

    private MoviesData mMoviesData;
    private TextView mRating;
    private TextView mReleaseDate;
    private WebView mOverview;
    private ImageView mPoster;
    private Typeface mTypeface;
    private ImageView mBackdrop;
    private ScrollView mScrollView;
    private RecyclerView mTrailersRecyclerView;
    private TrailerListAdapter mTrailerListAdapter;
    private RecyclerView mReviewsRecyclerView;
    private ReviewsListAdapter mReviewsListAdapter;
    private ImageView mFavouriteView;
    private boolean mIsFavourite;
    private android.support.v7.widget.ShareActionProvider mShareActionProvider;
    private int Y_SCROLL = 0;

    private void setScrollValueY(int value) {
        mScrollView.setScrollY(value);
    }

    private View.OnClickListener mFavouritesClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mIsFavourite) {
                showToast(getString(R.string.favourites_unmarked));
                mFavouriteView.setImageResource(R.mipmap.ic_unliked);
                removeFromFavorites();
            } else {
                showToast(getString(R.string.favourites_marked));
                mFavouriteView.setImageResource(R.mipmap.ic_liked);
                markAsFavorite();
            }
            mIsFavourite = !mIsFavourite;
            Animation pulse = AnimationUtils.loadAnimation(getContext(), R.anim.pulse);
            mFavouriteView.startAnimation(pulse);
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(AppConstants.APP_NAME, "onCreateView called");
        View layout = inflater.inflate(R.layout.fragment_single_movie, container, false);
        mTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
        Bundle bundle = this.getArguments();
        mMoviesData = bundle.getParcelable(AppConstants.INTENT_EXTRA_PARCEL);
        mRating = (TextView) layout.findViewById(R.id.rating_value_text);
        mReleaseDate = (TextView) layout.findViewById(R.id.release_date_value_text);
        mOverview = (WebView) layout.findViewById(R.id.overview_text);
        mPoster = (ImageView) layout.findViewById(R.id.thumbnail_image);
        mPoster.setAdjustViewBounds(true);
        mBackdrop = (ImageView) layout.findViewById(R.id.singleMovie_backdrop_image);
        mScrollView = (ScrollView) layout.findViewById(R.id.scrollView);
        mTrailersRecyclerView = (RecyclerView) layout.findViewById(R.id.trailers_recyclerView);
        mTrailersRecyclerView.setAdapter(new TrailerListAdapter(new ArrayList<TrailersData>()));
        mReviewsRecyclerView = (RecyclerView) layout.findViewById(R.id.reviews_recyclerView);
        mFavouriteView = (ImageView) layout.findViewById(R.id.favourite);
        mFavouriteView.setOnClickListener(mFavouritesClickListener);
        if(savedInstanceState != null)
            setScrollValueY(savedInstanceState.getInt(AppConstants.SCROLL_Y));

        if(!isNetworkAvailable())
            showToast(getString(R.string.network_unavailable_selected));
        updateViews(savedInstanceState);

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(AppConstants.APP_NAME, "onStart called");
        setHasOptionsMenu(true);
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void updateViews(Bundle savedInstance) {

        String url = AppConstants.POSTER_BASE_URL + AppConstants.POSTER_WIDTH_1 +
                mMoviesData.getMoviePosterPath();
        Picasso.with(getContext()).load(url).into(mPoster);

        url = AppConstants.POSTER_BASE_URL + AppConstants.POSTER_WIDTH_2 +
                mMoviesData.getMovieBackdropPath();
        Picasso.with(getContext()).load(url).fit().into(mBackdrop);
        mRating.setTypeface(mTypeface);
        Double rating = mMoviesData.getMovieRating();
        mRating.setText(rating.toString());

        mReleaseDate.setTypeface(mTypeface);
        mReleaseDate.setText(mMoviesData.getMovieReleaseDate());

        final String htmlFormat = "<html>" +
                "<body style=\"text-align:justify;" +
                "font-size: 20px;" +
                "font-family: roboto \"> %s" +
                " </body>" +
                "</html>";
        mOverview.getSettings().setDefaultTextEncodingName("utf-8");
        mOverview.loadData(String.format(htmlFormat, mMoviesData.getMovieOverview()), "text/html; charset=utf-8", "utf-8");
        mIsFavourite = isFavorite();
        if(mIsFavourite) {
            mFavouriteView.setImageResource(R.mipmap.ic_liked);
        } else {
            mFavouriteView.setImageResource(R.mipmap.ic_unliked);
        }

        new TrailerDataDownloader(mMoviesData.getMovieId(), this).start();
        new ReviewsDataDownloader(mMoviesData.getMovieId(), this).start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        getActivity().setTitle(mMoviesData.getMovieTitle());
        menuInflater.inflate(R.menu.single_movie_menu, menu);
        MenuItem share = menu.findItem(R.id.share);
        mShareActionProvider = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(share);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                if(!isNetworkAvailable())
                    showToast(getString(R.string.network_unavailable_selected));
                else {
                    showToast(getString(R.string.refreshing));
                    updateViews(null);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Y_SCROLL = mScrollView.getScrollY();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!isNetworkAvailable())
            showToast(getString(R.string.network_unavailable_selected));
        setScrollValueY(Y_SCROLL);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(AppConstants.SCROLL_Y, mScrollView.getScrollY());
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void removeFromFavorites() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (isFavorite()) {
                    getContext().getContentResolver().delete(MoviesDBContract.MovieDBEntry.CONTENT_URI,
                            MoviesDBContract.MovieDBEntry.MOVIE_ID + " = " + mMoviesData.getMovieId(), null);

                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void markAsFavorite() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (!isFavorite()) {
                    ContentValues movieValues = new ContentValues();
                    movieValues.put(MoviesDBContract.MovieDBEntry.MOVIE_ID,
                            mMoviesData.getMovieId());
                    movieValues.put(MoviesDBContract.MovieDBEntry.MOVIE_TITLE,
                            mMoviesData.getMovieTitle());
                    movieValues.put(MoviesDBContract.MovieDBEntry.MOVIE_POSTER_PATH,
                            mMoviesData.getMoviePosterPath());
                    movieValues.put(MoviesDBContract.MovieDBEntry.MOVIE_OVERVIEW,
                            mMoviesData.getMovieOverview());
                    movieValues.put(MoviesDBContract.MovieDBEntry.MOVIE_VOTE_AVERAGE,
                            mMoviesData.getMovieRating());
                    movieValues.put(MoviesDBContract.MovieDBEntry.MOVIE_RELEASE_DATE,
                            mMoviesData.getMovieReleaseDate());
                    movieValues.put(MoviesDBContract.MovieDBEntry.MOVIE_BACKDROP_PATH,
                            mMoviesData.getMovieBackdropPath());
                    getContext().getContentResolver().insert(
                            MoviesDBContract.MovieDBEntry.CONTENT_URI,
                            movieValues
                    );
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private boolean isFavorite() {
        Cursor movieCursor = getContext().getContentResolver().query(
                MoviesDBContract.MovieDBEntry.CONTENT_URI,
                new String[]{MoviesDBContract.MovieDBEntry.MOVIE_ID},
                MoviesDBContract.MovieDBEntry.MOVIE_ID+ " = " + mMoviesData.getMovieId(),
                null,
                null);

        if (movieCursor != null && movieCursor.moveToFirst()) {
            movieCursor.close();
            return true;
        } else {
            return false;
        }
    }
    private void updateShareActionProvider(TrailersData trailer) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mMoviesData.getMovieTitle());
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, trailer.getName() + ": "
                + trailer.getTrailerUrl());
        mShareActionProvider.setShareIntent(sharingIntent);
    }
    @Override
    public void downloadedTrailersData(List<TrailersData> moviesTrailersArrayList) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mTrailersRecyclerView.setLayoutManager(layoutManager);
        mTrailerListAdapter = new TrailerListAdapter(new ArrayList<>(moviesTrailersArrayList));
        mTrailersRecyclerView.setAdapter(mTrailerListAdapter);
        mTrailersRecyclerView.setNestedScrollingEnabled(false);
        updateShareActionProvider(moviesTrailersArrayList.get(0));
    }

    @Override
    public void downloadedReviewsData(List<ReviewsData> moviesReviewArrayList) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        Log.d(AppConstants.APP_NAME, ""+moviesReviewArrayList.size());
        mReviewsRecyclerView.setLayoutManager(layoutManager);
        mReviewsListAdapter = new ReviewsListAdapter(new ArrayList<>(moviesReviewArrayList));
        mReviewsRecyclerView.setAdapter(mReviewsListAdapter);
        mReviewsRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void downloadedMoviesList(List<MoviesData> moviesDataArrayList) {

    }

    @Override
    public Call<TrailersList> getTrailers(@Path("id") long id, @Query("api_key") String apiKey) {
        return null;
    }

    @Override
    public Call<ReviewsList> getReviews(@Path("id") long id, @Query("api_key") String apiKey) {
        return null;
    }
}
