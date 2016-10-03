package com.diraj.popularmovies.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.diraj.popularmovies.AppConstants;
import com.diraj.popularmovies.R;
import com.diraj.popularmovies.movies.MoviesData;

public class SingleMovieActivity extends AppCompatActivity {

    private MoviesData mMoviesData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie);

        Bundle bundle = getIntent().getExtras();
        mMoviesData = bundle.getParcelable(AppConstants.INTENT_EXTRA_PARCEL);

        if(getActionBar() != null) {
            getActionBar().setTitle(mMoviesData.getMovieTitle());
        }
        if(savedInstanceState == null) {
            Bundle args = new Bundle();
            args.putParcelable(AppConstants.INTENT_EXTRA_PARCEL, getIntent().getExtras().getParcelable(AppConstants.INTENT_EXTRA_PARCEL));
            SingleMovieFragment singleMovieFragment = new SingleMovieFragment();
            singleMovieFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_single_movie, singleMovieFragment).commit();
            getSupportFragmentManager().executePendingTransactions();
        }
    }
}
