/**
 * Created by Diraj H S on 9/25/16.
 * Copyright (c) 2016. All rights reserved.
 */
package com.diraj.popularmovies.trailer;

import android.os.Parcel;
import android.os.Parcelable;

import com.diraj.popularmovies.AppConstants;
import com.google.gson.annotations.SerializedName;

public class TrailersData implements Parcelable{

    @SerializedName("id")
    private String mId;
    @SerializedName("key")
    private String mKey;
    @SerializedName("name")
    private String mName;
    @SerializedName("site")
    private String mSite;
    @SerializedName("size")
    private String mSize;

    public String getName() {
        return mName;
    }

    public String getId() {
        return mId;
    }

    public String getKey() {
        return mKey;
    }

    public String getSite() {
        return mSite;
    }

    public String getSize() {
        return mSize;
    }

    public String getTrailerUrl() {
        return AppConstants.TRAILER_URL + mKey;
    }

    public static final Parcelable.Creator<TrailersData> CREATOR = new Creator<TrailersData>() {
        public TrailersData createFromParcel(Parcel source) {
            TrailersData TrailersData = new TrailersData();
            TrailersData.mId = source.readString();
            TrailersData.mKey = source.readString();
            TrailersData.mName = source.readString();
            TrailersData.mSite = source.readString();
            TrailersData.mSize = source.readString();
            return TrailersData;
        }

        public TrailersData[] newArray(int size) {
            return new TrailersData[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mId);
        parcel.writeString(mKey);
        parcel.writeString(mName);
        parcel.writeString(mSite);
        parcel.writeString(mSize);
    }

}
