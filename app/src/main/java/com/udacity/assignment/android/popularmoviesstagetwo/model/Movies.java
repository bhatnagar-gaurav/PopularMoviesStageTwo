package com.udacity.assignment.android.popularmoviesstagetwo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.udacity.assignment.android.popularmoviesstagetwo.utilities.Constants;

/************* Class required for parsing the response of the Webservice ********************/


public class Movies implements Parcelable {
    @SerializedName(Constants.JSON_MOVIE_TITLE)
    private String movieTitle;
    @SerializedName(Constants.JSON_MOVIE_ID)
    private String movieId;
    @SerializedName(Constants.JSON_MOVIE_THUMBNAIL_PATH)
    private String thumbnailURL;
    @SerializedName(Constants.JSON_MOVIE_OVERVIEW)
    private String synopsis;
    @SerializedName(Constants.JSON_MOVIE_VOTE_AVERAGE)
    private String avgVoteCount;
    @SerializedName(Constants.JSON_MOVIE_RELEASE_DATE)
    private String releaseDate;
    @SerializedName("vote_count")
    private String totalVoteCount;

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getAvgVoteCount() {
        return avgVoteCount;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTotalVoteCount() {
        return totalVoteCount;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.movieTitle);
        dest.writeString(this.movieId);
        dest.writeString(this.thumbnailURL);
        dest.writeString(this.synopsis);
        dest.writeString(this.avgVoteCount);
        dest.writeString(this.releaseDate);
        dest.writeString(this.totalVoteCount);
    }

    public Movies() {
    }

    public Movies(String specificThumbnailURL, String specificSynopsis, String specificReleaseDate, String specificMovieID,
                  String specificMovieTitle, String specificAvgVoteCount) {
        thumbnailURL = specificThumbnailURL;
        synopsis = specificSynopsis;
        releaseDate = specificReleaseDate;
        movieId = specificMovieID;
        movieTitle = specificMovieTitle;
        avgVoteCount = specificAvgVoteCount;
    }

    protected Movies(Parcel in) {
        this.movieTitle = in.readString();
        this.movieId = in.readString();
        this.thumbnailURL = in.readString();
        this.synopsis = in.readString();
        this.avgVoteCount = in.readString();
        this.releaseDate = in.readString();
        this.totalVoteCount = in.readString();
    }

    public static final Parcelable.Creator<Movies> CREATOR = new Parcelable.Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel source) {
            return new Movies(source);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };
}
