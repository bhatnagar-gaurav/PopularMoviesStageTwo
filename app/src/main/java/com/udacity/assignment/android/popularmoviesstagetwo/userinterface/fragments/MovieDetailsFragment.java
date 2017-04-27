package com.udacity.assignment.android.popularmoviesstagetwo.userinterface.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.udacity.assignment.android.popularmoviesstagetwo.R;
import com.udacity.assignment.android.popularmoviesstagetwo.model.MovieReviews;
import com.udacity.assignment.android.popularmoviesstagetwo.model.MovieReviewsResponse;
import com.udacity.assignment.android.popularmoviesstagetwo.model.MovieTrailerVideosResponse;
import com.udacity.assignment.android.popularmoviesstagetwo.model.TrailerVideos;
import com.udacity.assignment.android.popularmoviesstagetwo.utilities.Constants;
import com.udacity.assignment.android.popularmoviesstagetwo.utilities.DateUtils;
import com.udacity.assignment.android.popularmoviesstagetwo.utilities.networkutils.RetrofitApiClient;
import com.udacity.assignment.android.popularmoviesstagetwo.utilities.networkutils.RetrofitApiInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.udacity.assignment.android.popularmoviesstagetwo.utilities.Constants.API_KEY;
import static com.udacity.assignment.android.popularmoviesstagetwo.utilities.Constants.ARG_SELECTED_DETAIL;

public class MovieDetailsFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = MovieDetailsFragment.class.getSimpleName();

    private int selectedPosition = -1;
    private static String selectedMovieThumbnailPath, selectedMovieSynopsis,
            selectedMovieReleaseDate, selectedMovieAvgVoteCount, selectedMovieTitle,
            selectedMovieId, selectedMovieTotalVoteCount;

    @BindView(R.id.movie_detail_thumbnail)
    ImageView movieDetailThumbnail;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.movieTitle)
    TextView movieTitle;
    @BindView(R.id.movieReleaseDate)
    TextView movieReleaseDate;
    @BindView(R.id.movieTotalVoteCount)
    TextView movieTotalVoteCount;
    @BindView(R.id.movieSynopsis)
    TextView movieSynopsis;
    @BindView(R.id.movieVoteCountAverage)
    TextView movieVoteAverage;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.movie_detail_reviews_linear_layout)
    LinearLayout movieDetailsReviewsLinearLayout;
    private String youTubeURL;
    private ArrayList<TrailerVideos> moviesDetailTrailerVideosList;
    private ArrayList<MovieReviews> moviesDetailReviewsArrayList;
    private int outOfBoundIndex;

    public MovieDetailsFragment() {
    }

    /**
     * Creates the new instance of this screen with all the required parameters of the selected Movie.
     * @param thumbnailURL                The URL for the Selected Movie thumbnail
     * @param movieSynopsis              The Synopsis of the Selected Movie
     * @param movieReleaseDate           The Release Date of the Selected Movie.
     * @param movieId                    The Unique Identifier for the Selected Movie.
     * @param movieTitle                 The Title of the Selected Movie
     * @param voteCountAverage           The Average Vote Count for the Selected Movie
     * @param totalVoteCount             The Total Vote Count for the Selected Movie.
     */
    public static void newInstance(String thumbnailURL, String movieSynopsis, String movieReleaseDate,
                                   String movieId, String movieTitle, String voteCountAverage,
                                   String totalVoteCount) {
        selectedMovieThumbnailPath = thumbnailURL;
        selectedMovieSynopsis = movieSynopsis;
        selectedMovieReleaseDate = movieReleaseDate;
        selectedMovieId = movieId;
        selectedMovieTitle = movieTitle;
        selectedMovieAvgVoteCount = voteCountAverage;
        selectedMovieTotalVoteCount = totalVoteCount;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (!tabletSize) {
            // Incorporating the Back Button by Up Navigation
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        else{
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        toolbar.setTitle(R.string.movie_detail_title);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            selectedPosition = savedInstanceState.getInt(ARG_SELECTED_DETAIL);
        }
        View detailView = inflater.inflate(R.layout.details_activity_layout, container, false);
        ButterKnife.bind(this, detailView);
        movieDetailsReviewsLinearLayout.removeAllViews();
        return detailView;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    /**
     * Updates the Screen with the modified Movie Selection in the GridView
     *
     * @param position The selected position of the movie in the GridView shown before.
     */

    public void updateMovieDetailsView(int position) {
        selectedPosition = position;
        populateUI();
        populateYoutubeUrl();
        populateMovieReviews();
    }

    /**
     * Populates the Screen with all the movie details.
     */

    public void populateUI() {

        try {
            Glide.clear(movieDetailThumbnail);
            Glide.with(getActivity())
                    .load(Constants.FINAL_THUMBNAIL_URI + selectedMovieThumbnailPath)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            Log.e(TAG, "Exception occurring while rendering the Thumbnail");
                            return false;

                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model,
                                                       Target<GlideDrawable> target,
                                                       boolean isFromMemoryCache, boolean isFirstResource) {

                            return false;
                        }
                    })
                    .into(movieDetailThumbnail);
            movieTitle.setText(selectedMovieTitle);
            String formattedDate = DateUtils.convertDate(selectedMovieReleaseDate);
            movieReleaseDate.setText(formattedDate);
            movieVoteAverage.setText(selectedMovieAvgVoteCount.concat(getString(R.string.movie_average_vote_count_suffix)));
            movieSynopsis.setText(selectedMovieSynopsis);
            selectedMovieTotalVoteCount = selectedMovieTotalVoteCount + " ";
            movieTotalVoteCount.setText(selectedMovieTotalVoteCount.concat(getString(R.string.movie_total_vote_count_suffix)));
        } catch (Exception e) {
            Log.e(TAG, "Exception while populating the movie Details");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        updateMovieDetailsUsingArguments();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieDetailsUsingArguments();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        updateMovieDetailsUsingArguments();
        outState.putInt(ARG_SELECTED_DETAIL, selectedPosition);
    }

    private void updateMovieDetailsUsingArguments() {
        Bundle args = getArguments();
        if (args != null) {
            updateMovieDetailsView(args.getInt(ARG_SELECTED_DETAIL));
        } else if (selectedPosition != -1) {
            updateMovieDetailsView(selectedPosition);
        }
    }

    private void populateYoutubeUrl() {
        RetrofitApiInterface apiService =
                RetrofitApiClient.getClient(getActivity()).create(RetrofitApiInterface.class);
        Call<MovieTrailerVideosResponse> trailerVideoCall = apiService.getMovieTrailerVideos(Integer.parseInt(selectedMovieId), API_KEY);
        trailerVideoCall.enqueue(new Callback<MovieTrailerVideosResponse>() {
            @Override
            public void onResponse(Call<MovieTrailerVideosResponse> call, Response<MovieTrailerVideosResponse> response) {
                if (moviesDetailTrailerVideosList != null)
                    moviesDetailTrailerVideosList.clear();
                moviesDetailTrailerVideosList = response.body().getTrailerVideos();
                try {
                    youTubeURL = Constants.BASIC_YOUTUBE_URI + moviesDetailTrailerVideosList.get(0).getYtKey();
                } catch (IndexOutOfBoundsException e) {
                    outOfBoundIndex = 1;
                    Toast.makeText(getActivity(), getString(R.string.zero_movie_trailers), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieTrailerVideosResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    private void populateMovieReviews() {
        RetrofitApiInterface reviewsApiService =
                RetrofitApiClient.getClient(getActivity()).create(RetrofitApiInterface.class);
        Call<MovieReviewsResponse> movieReviewCall = reviewsApiService.getMovieReviews(Integer.parseInt(selectedMovieId), API_KEY);
        movieReviewCall.enqueue(new Callback<MovieReviewsResponse>() {
            @Override
            public void onResponse(Call<MovieReviewsResponse> call, Response<MovieReviewsResponse> response) {

                if (response.body().getTotalResults() != 0) {
                    movieDetailsReviewsLinearLayout.removeAllViews();
                    if (moviesDetailReviewsArrayList != null)
                        moviesDetailReviewsArrayList.clear();
                    moviesDetailReviewsArrayList = response.body().getMovieReviews();
                    makeReviewsTextviews();
                } else {
                    movieDetailsReviewsLinearLayout.removeAllViews();
                    noReviewsTextViews();
                }
            }

            @Override
            public void onFailure(Call<MovieReviewsResponse> errorCall, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    private void noReviewsTextViews() {
        try {
            TextView textView1 = new TextView(getActivity());
            textView1.setTextSize(20);
            textView1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            textView1.setText(getResources().getString(R.string.zero_movie_review));
            textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
            movieDetailsReviewsLinearLayout.addView(textView1);
        } catch (NullPointerException ignored) {
            Log.e(TAG, "Error while defining no reviews values");
        }
    }

    private void makeReviewsTextviews() {
        TextView[] textViews = new TextView[moviesDetailReviewsArrayList.size()];
        TextView[] textViewsAuthors = new TextView[moviesDetailReviewsArrayList.size()];
        for (int i = 0; i < moviesDetailReviewsArrayList.size(); i++) {
            try {
                textViewsAuthors[i] = new TextView(getActivity());
                textViews[i] = new TextView(getActivity());

                textViewsAuthors[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                textViews[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                textViewsAuthors[i].setText(moviesDetailReviewsArrayList.get(i).getAuthor());
                textViewsAuthors[i].setTextSize(18);
                textViews[i].setText(moviesDetailReviewsArrayList.get(i).getContent());

                textViewsAuthors[i].setTypeface(null, Typeface.BOLD);
                if (getResources().getBoolean(R.bool.isTablet)) {
                    textViewsAuthors[i].setPadding(40, 5, 10, 5);
                    textViews[i].setPadding(60, 5, 60, 10);// in pixels (left, top, right, bottom )
                } else {
                    textViewsAuthors[i].setPadding(10, 5, 10, 5);
                    textViews[i].setPadding(20, 5, 20, 10);// in pixels (left, top, right, bottom )
                }
                Linkify.addLinks(textViews[i], Linkify.ALL);

                movieDetailsReviewsLinearLayout.addView(textViewsAuthors[i]);
                movieDetailsReviewsLinearLayout.addView(textViews[i]);

            } catch (NullPointerException ignored) {
                Log.e(TAG, "Error while defining reviews values");
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.menu_movie_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    /**
     * Uses the ShareCompat Intent builder to create our Sharing Youtube intent for sharing. We set the
     * type of content that we are sharing (just regular text), the youtube URL itself, for the
     * newly created Intent.

     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_share:
                if (!TextUtils.isEmpty(youTubeURL)){
                    Intent intent = ShareCompat.IntentBuilder.from(getActivity()).setChooserTitle("How will you like to share").setType("text/plain")
                            .setText(youTubeURL).createChooserIntent();
                    item.setIntent(intent);
                }
                else {
                    Toast.makeText(getActivity(), getString(R.string.zero_movie_trailers), Toast.LENGTH_SHORT).show();

                }
        }
        return false;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
           /* case R.id.backButton:
                getActivity().onBackPressed();
                break;*/
            default:
                break;
        }
    }


}
