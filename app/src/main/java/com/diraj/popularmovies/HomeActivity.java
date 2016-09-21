/**
 * Created by Diraj H S on 9/13/16.
 * Copyright (c) 2016. All rights reserved.
 */
package com.diraj.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements MoviesList {

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
        intent.putExtra(AppConstants.Intent_Extra_Parcel, object);
        startActivity(intent);
        AnimationHandling.animateScreen(this, AnimationHandling.ANIM_TYPE.START);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimationHandling.animateScreen(this, AnimationHandling.ANIM_TYPE.CLOSE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menus, menu);
        return super.onCreateOptionsMenu(menu);
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
                } else {
                    showToast(getString(R.string.toast_top_rated));
                }
                startDownloadAndDisplayThread(mToggleSort);
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
            mMovieList = savedInstance.getParcelableArrayList(AppConstants.Movies_List);
            mToggleSort = savedInstance.getBoolean(AppConstants.Toggle_Sort);
            mGridSelectedPosition = savedInstance.getInt(AppConstants.Grid_Position);
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
        outState.putInt(AppConstants.Grid_Position, mGridView.getFirstVisiblePosition());
        outState.putBoolean(AppConstants.Toggle_Sort, mToggleSort);
        ArrayList<MoviesData> moviesDatas = new ArrayList<>(mMovieList);
        outState.putParcelableArrayList(AppConstants.Movies_List, moviesDatas);
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
}
