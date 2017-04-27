package com.udacity.assignment.android.popularmoviesstagetwo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MovieReviews implements Parcelable {
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.author);
        dest.writeString(this.content);
    }

    public MovieReviews() {
    }

    protected MovieReviews(Parcel in) {
        this.author = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<MovieReviews> CREATOR = new Parcelable.Creator<MovieReviews>() {
        @Override
        public MovieReviews createFromParcel(Parcel source) {
            return new MovieReviews(source);
        }

        @Override
        public MovieReviews[] newArray(int size) {
            return new MovieReviews[size];
        }
    };
}
