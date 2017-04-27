package com.udacity.assignment.android.popularmoviesstagetwo.model;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MovieTrailerVideosResponse {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private ArrayList<TrailerVideos> trailerVideos;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public ArrayList<TrailerVideos> getTrailerVideos() {
        return trailerVideos;
    }
}
