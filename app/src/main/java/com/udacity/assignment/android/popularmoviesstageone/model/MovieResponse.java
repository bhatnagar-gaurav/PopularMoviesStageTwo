package com.udacity.assignment.android.popularmoviesstageone.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


/************* Class for consuming the response of the Webservices ********************/

public class MovieResponse implements Serializable {

    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private ArrayList<Movies> results;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public ArrayList<Movies> getResults() {
        return results;
    }

}
