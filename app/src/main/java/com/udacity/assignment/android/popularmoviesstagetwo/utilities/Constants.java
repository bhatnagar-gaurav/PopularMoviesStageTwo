package com.udacity.assignment.android.popularmoviesstagetwo.utilities;

import com.udacity.assignment.android.popularmoviesstagetwo.BuildConfig;

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

    /****************************************************************
     ******* Constants Used For Building the Movie Trailer Video *********
     ****************************************************************/

    private static final String VIDEO_THUMBNAIL_SIZE_W500 = "w500/";
    public static final String FINAL_TRAILER_VIDEO_THUMBNAIL_URI = THUMBNAIL_URI+VIDEO_THUMBNAIL_SIZE_W500;
    public static final String BASIC_YOUTUBE_URI = "https://www.youtube.com/watch?v=";

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
    public final static String JSON_MOVIE_VIDEO_PATH = "backdrop_path";


}
