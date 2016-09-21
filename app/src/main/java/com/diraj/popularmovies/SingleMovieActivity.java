/**
 * Created by Diraj H S on 9/13/16.
 * Copyright (c) 2016. All rights reserved.
 */
package com.diraj.popularmovies;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class SingleMovieActivity extends AppCompatActivity {

    private MoviesData mMoviesData;
    private TextView mRating;
    private TextView mReleaseDate;
    private WebView mOverview;
    private ImageView mPoster;
    private Typeface mTypeface;
    private ImageView mBackdrop;
    private ScrollView mScrollView;
    private int Y_SCROLL = 0;

    private void setScrollValueY(int value) {
        mScrollView.setScrollY(value);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie);

        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        mMoviesData = getIntent().getExtras().getParcelable(AppConstants.Intent_Extra_Parcel);
        mRating = (TextView) findViewById(R.id.rating_value_text);
        mReleaseDate = (TextView) findViewById(R.id.release_date_value_text);
        mOverview = (WebView) findViewById(R.id.overview_text);
        mPoster = (ImageView) findViewById(R.id.thumbnail_image);
        mPoster.setAdjustViewBounds(true);
        mBackdrop = (ImageView) findViewById(R.id.singleMovie_backdrop_image);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);

        if(savedInstanceState != null)
            setScrollValueY(savedInstanceState.getInt(AppConstants.Scroll_Y));

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mMoviesData.getMovieTitle());
        }
        if(!isNetworkAvailable())
            showToast(getString(R.string.network_unavailable_selected));
        updateViews();
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void updateViews() {

        String url = AppConstants.Poster_Base_URL + AppConstants.Poster_width_1 +
                mMoviesData.getMoviePosterPath();
        Picasso.with(getApplicationContext()).load(url).into(mPoster);

        url = AppConstants.Poster_Base_URL + AppConstants.Poster_width_2 +
                mMoviesData.getMovieBackdropPath();
        Picasso.with(getApplicationContext()).load(url).fit().into(mBackdrop);
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
        mOverview.loadData(String.format(htmlFormat, mMoviesData.getMovieOverview()), "text/html", "utf-8");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimationHandling.animateScreen(this, AnimationHandling.ANIM_TYPE.CLOSE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_movie_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                if(!isNetworkAvailable())
                    showToast(getString(R.string.network_unavailable_selected));
                else {
                    showToast(getString(R.string.refreshing));
                    updateViews();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Y_SCROLL = mScrollView.getScrollY();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isNetworkAvailable())
            showToast(getString(R.string.network_unavailable_selected));
        setScrollValueY(Y_SCROLL);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(AppConstants.Scroll_Y, mScrollView.getScrollY());
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
