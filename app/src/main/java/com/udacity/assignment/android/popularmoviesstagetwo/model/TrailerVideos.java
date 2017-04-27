package com.udacity.assignment.android.popularmoviesstagetwo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TrailerVideos implements Parcelable {
    @SerializedName("key")
    private String ytKey;
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYtKey() {
        return ytKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ytKey);
        dest.writeString(this.name);
    }

    public TrailerVideos() {
    }

    protected TrailerVideos(Parcel in) {
        this.ytKey = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<TrailerVideos> CREATOR = new Parcelable.Creator<TrailerVideos>() {
        @Override
        public TrailerVideos createFromParcel(Parcel source) {
            return new TrailerVideos(source);
        }

        @Override
        public TrailerVideos[] newArray(int size) {
            return new TrailerVideos[size];
        }
    };
}
