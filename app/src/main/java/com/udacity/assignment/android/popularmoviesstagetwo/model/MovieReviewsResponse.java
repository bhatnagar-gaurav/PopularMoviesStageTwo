package com.udacity.assignment.android.popularmoviesstagetwo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class MovieReviewsResponse {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private ArrayList<MovieReviews> movieReviews;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public ArrayList<MovieReviews> getMovieReviews() {
        return movieReviews;
    }

    public int getTotalResults() {
        return totalResults;
    }

}
