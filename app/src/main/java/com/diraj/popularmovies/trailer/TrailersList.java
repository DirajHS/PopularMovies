/**
 * Created by Diraj H S on 9/25/16.
 * Copyright (c) 2016. All rights reserved.
 */
package com.diraj.popularmovies.trailer;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TrailersList {

    @SerializedName("results")
    private List<TrailersData>  mTrailers = new ArrayList<>();

    public List<TrailersData> getTrailers() {
        return mTrailers;
    }
}
