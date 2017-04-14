package com.udacity.assignment.android.popularmoviesstageone.utilities.networkutils;


import com.udacity.assignment.android.popularmoviesstageone.model.MovieResponse;
import com.udacity.assignment.android.popularmoviesstageone.utilities.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitApiInterface {

    String topRatedURL = Constants.TAG_MOVIE+(Constants.TAG_TOP_RATED);
    String popularURL = Constants.TAG_MOVIE+(Constants.TAG_POPULAR);

    @GET(topRatedURL)
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET(popularURL)
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);
}
