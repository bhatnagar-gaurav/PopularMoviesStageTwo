package com.udacity.assignment.android.popularmoviesstageone.userinterface.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.udacity.assignment.android.popularmoviesstageone.R;
import com.udacity.assignment.android.popularmoviesstageone.utilities.Constants;
import com.udacity.assignment.android.popularmoviesstageone.utilities.DateUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.assignment.android.popularmoviesstageone.utilities.Constants.ARG_SELECTED_DETAIL;

public class MovieDetailsFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = MovieDetailsFragment.class.getSimpleName();

    private int selectedPosition = -1;
    private static String selectedMovieThumbnailPath, selectedMovieSynopsis,
            selectedMovieReleaseDate, selectedMovieAvgVoteCount, selectedMovieTitle,
            selectedMovieId, selectedMovieTotalVoteCount;
    @BindView(R.id.movie_detail_thumbnail)
    ImageView movieDetailThumbnail;
    @Nullable @BindView(R.id.backButton)
    Button backButton;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            selectedPosition = savedInstanceState.getInt(ARG_SELECTED_DETAIL);
        }
        View detailView = inflater.inflate(R.layout.details_activity_layout, container, false);
        ButterKnife.bind(this, detailView);
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (!tabletSize) {
            if (backButton != null) {
                backButton.setVisibility(View.VISIBLE);
                backButton.setOnClickListener(this);
            }
        }
        else{
            backButton.setVisibility(View.GONE);
        }
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



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                getActivity().onBackPressed();
                break;
            default:
                break;
        }
    }


}
