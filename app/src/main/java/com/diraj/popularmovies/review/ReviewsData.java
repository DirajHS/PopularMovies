/**
 * Created by Diraj H S on 9/25/16.
 * Copyright (c) 2016. All rights reserved.
 */

package com.diraj.popularmovies.review;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ReviewsData implements Parcelable{

    @SerializedName("id")
    private String mId;
    @SerializedName("author")
    private String mAuthor;
    @SerializedName("content")
    private String mContent;
    @SerializedName("url")
    private String mUrl;

    public String getContent() {
        return mContent;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getUrl() {
        return mUrl;
    }

    public static final Parcelable.Creator<ReviewsData> CREATOR = new Creator<ReviewsData>() {
        public ReviewsData createFromParcel(Parcel source) {
            ReviewsData review = new ReviewsData();
            review.mId = source.readString();
            review.mAuthor = source.readString();
            review.mContent = source.readString();
            review.mUrl = source.readString();
            return review;
        }

        public ReviewsData[] newArray(int size) {
            return new ReviewsData[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mId);
        parcel.writeString(mAuthor);
        parcel.writeString(mContent);
        parcel.writeString(mUrl);
    }
}
