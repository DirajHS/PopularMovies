/**
 * Created by Diraj H S on 9/25/16.
 * Copyright (c) 2016. All rights reserved.
 */

package com.diraj.popularmovies.review;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ReviewsList {

    @SerializedName("results")
    public List<ReviewsData> mReviewsList = new ArrayList<>();

    public List<ReviewsData> getReviews() {
        return mReviewsList;
    }
}
