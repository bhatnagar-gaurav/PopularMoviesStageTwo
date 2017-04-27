package com.udacity.assignment.android.popularmoviesstagetwo.utilities.networkutils;


import com.udacity.assignment.android.popularmoviesstagetwo.model.MovieResponse;
import com.udacity.assignment.android.popularmoviesstagetwo.model.MovieReviewsResponse;
import com.udacity.assignment.android.popularmoviesstagetwo.model.MovieTrailerVideosResponse;
import com.udacity.assignment.android.popularmoviesstagetwo.utilities.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitApiInterface {

    String topRatedURL = Constants.TAG_MOVIE+(Constants.TAG_TOP_RATED);
    String popularURL = Constants.TAG_MOVIE+(Constants.TAG_POPULAR);

    @GET(topRatedURL)
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET(popularURL)
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);


    @GET("movie/{id}/videos")
    Call<MovieTrailerVideosResponse> getMovieTrailerVideos(@Path(Constants.JSON_MOVIE_ID) int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<MovieReviewsResponse> getMovieReviews(@Path(Constants.JSON_MOVIE_ID) int id, @Query("api_key") String apiKey);
}
