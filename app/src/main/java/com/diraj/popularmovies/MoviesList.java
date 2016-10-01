/**
 * Created by Diraj H S on 9/21/16.
 * Copyright (c) 2016. All rights reserved.
 */
package com.diraj.popularmovies;

import com.diraj.popularmovies.movies.MoviesData;
import com.diraj.popularmovies.review.ReviewsData;
import com.diraj.popularmovies.review.ReviewsList;
import com.diraj.popularmovies.trailer.TrailersData;
import com.diraj.popularmovies.trailer.TrailersList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesList {

    void downloadedMoviesList(List<MoviesData> moviesDataArrayList);

    void downloadedTrailersData(List<TrailersData> moviesTrailersArrayList);

    void downloadedReviewsData(List<ReviewsData> moviesReviewArrayList);

    @GET("3/movie/{id}/videos")
    Call<TrailersList> getTrailers(@Path("id") long id, @Query("api_key") String apiKey);

    @GET("3/movie/{id}/reviews")
    Call<ReviewsList> getReviews(@Path("id") long id, @Query("api_key") String apiKey);
}
