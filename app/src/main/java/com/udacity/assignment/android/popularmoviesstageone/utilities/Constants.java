package com.udacity.assignment.android.popularmoviesstageone.utilities;

import com.udacity.assignment.android.popularmoviesstageone.BuildConfig;

public class Constants {


    public static final int PORTRAIT_GRID = 2;
    public static final int LANDSCAPE_GRID = 3;

    /***************************************************
     ******* Constants Used For Building the URL*********
     ****************************************************/


    public static final String BASE_URI = "http://api.themoviedb.org/3/";
    public static final String TAG_MOVIE = "movie/";
    public static final String TAG_POPULAR = "popular";
    public static final String TAG_TOP_RATED = "top_rated";
    public static final String API_KEY = BuildConfig.MOVIE_DB_API_KEY;

    /****************************************************************
     ******* Constants Used For Building the Movie Thumbnail*********
     ****************************************************************/


    private static final String THUMBNAIL_URI = "http://image.tmdb.org/t/p/";
    private static final String THUMBNAIL_SIZE_W185 = "w185/";
    public static final String FINAL_THUMBNAIL_URI = THUMBNAIL_URI+THUMBNAIL_SIZE_W185;

    /*********************************************************************************
     ******* Constants Used For Populating the Selected Movie Detail Screen **********
     *********************************************************************************/


    public final static String ARG_SELECTED_DETAIL = "position";



    /***************************************************
     ******* Constants Used For Parsing the Response*********
    ****************************************************/

    public final static String JSON_MOVIE_THUMBNAIL_PATH = "poster_path";
    public final static String JSON_MOVIE_OVERVIEW = "overview";
    public final static String JSON_MOVIE_RELEASE_DATE = "release_date";
    public final static String JSON_MOVIE_ID = "id";
    public final static String JSON_MOVIE_TITLE = "original_title";
    public final static String JSON_MOVIE_VOTE_AVERAGE = "vote_average";


}
