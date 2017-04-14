package com.udacity.assignment.android.popularmoviesstageone.userinterface.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.udacity.assignment.android.popularmoviesstageone.R;
import com.udacity.assignment.android.popularmoviesstageone.model.MovieResponse;
import com.udacity.assignment.android.popularmoviesstageone.model.Movies;
import com.udacity.assignment.android.popularmoviesstageone.utilities.adapters.GridMoviesAdapter;
import com.udacity.assignment.android.popularmoviesstageone.utilities.itemdecoration.ItemDecoration;
import com.udacity.assignment.android.popularmoviesstageone.utilities.networkutils.NetworkUtilFunctions;
import com.udacity.assignment.android.popularmoviesstageone.utilities.networkutils.RetrofitApiClient;
import com.udacity.assignment.android.popularmoviesstageone.utilities.networkutils.RetrofitApiInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.udacity.assignment.android.popularmoviesstageone.utilities.Constants.API_KEY;
import static com.udacity.assignment.android.popularmoviesstageone.utilities.Constants.LANDSCAPE_GRID;
import static com.udacity.assignment.android.popularmoviesstageone.utilities.Constants.PORTRAIT_GRID;

public class TopRatedMoviesFragment extends Fragment implements GridMoviesAdapter.GridAdapterOnClickHandler{

    private static final String TAG = TopRatedMoviesFragment.class.getSimpleName();

    private static final String BUNDLE_MOVIES = "BUNDLE_MOVIES";
    GridLayoutManager gridLayoutManager;
    GridMoviesAdapter gridMoviesAdapter;
    PopularMoviesFragment.OnMovieItemClickListener onItemClickCallback;
    private int scrollPosition;
    private ArrayList<Movies> movies = new ArrayList<>();
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView_movies)
    RecyclerView recyclerView;
    @BindView(R.id.error_view_no_internet)
    View errorView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private static int usingSavedInstance;
    private ArrayList<Movies> savedMovies;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View highestRatedMovieGrid= inflater.inflate(R.layout.popular_movies_list, container, false);

        ButterKnife.bind(this, highestRatedMovieGrid);

        errorView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        recyclerView.setHasFixedSize(true);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(getActivity(), PORTRAIT_GRID);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            gridLayoutManager = new GridLayoutManager(getActivity(), LANDSCAPE_GRID);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        recyclerView.addItemDecoration(new ItemDecoration(1, 1, false));

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.orange_swipe_refresh),
                ContextCompat.getColor(getActivity(),R.color.green_swipe_refresh),
                ContextCompat.getColor(getActivity(),R.color.cyan_swipe_refresh));

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            savedMovies = savedInstanceState.getParcelableArrayList(BUNDLE_MOVIES);
            gridMoviesAdapter = new GridMoviesAdapter(savedMovies,this, getActivity());
            recyclerView.setAdapter(gridMoviesAdapter);
            usingSavedInstance = 1;
        } else {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    if (NetworkUtilFunctions.isConnected(getActivity())) {
                         /* Enable the Refresh for swipeLayout  */
                        swipeRefreshLayout.setRefreshing(true);
                        showHighestRatedMoviesGrid();
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
                    } else {
                        showErrorMessage();
                        Toast.makeText(getActivity(), getString(R.string.error_no_internet_sub_title), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtilFunctions.isConnected(getActivity())) {
                    showHighestRatedMoviesGrid();
                } else {
                    showErrorMessage();
                    Toast.makeText(getActivity(), getString(R.string.error_no_internet_sub_title), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return highestRatedMovieGrid;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (usingSavedInstance == 1) {
            outState.putParcelableArrayList(BUNDLE_MOVIES, savedMovies);
        } else {
            outState.putParcelableArrayList(BUNDLE_MOVIES, movies);
        }
    }

    @Override
    public void onAttach(Activity accessActivity) {
        super.onAttach(accessActivity);

        try {
            onItemClickCallback = (PopularMoviesFragment.OnMovieItemClickListener) accessActivity;
        } catch (ClassCastException e) {
            Log.e(TAG,e.toString());
            throw new ClassCastException(accessActivity.toString() + getString(R.string.fragment_attachment_exception));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        inflater.inflate(R.menu.menu_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_by_popular_sort) {
            PopularMoviesFragment fragment = new PopularMoviesFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content, fragment).commit();
            return true;
        }
        if (id == R.id.action_by_top_rating_sort) {
            Toast.makeText(getActivity(), getString(R.string.menu_options_top_rated_message), Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    /**
     * This method will make the RecyclerView for Highest Rated Movies visible and
     * hide the error message.
     */
    private void showHighestRatedMoviesGrid() {
        /* First, We have to make sure the error is invisible and the Grid is visible */
        errorView.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        /* Then, make sure the Grid of Highest Rated Movies is visible */
        loadHighestRatedMovies();
    }



    /**
     * This method will make the error message visible and hide the Grid of Highest Rated Movies
     */
    private void showErrorMessage() {
        /* Disable the refresh if swipeRefresh is enabled */
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
        /* First, hide the Grid of Highest Rated Movies */
        recyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        errorView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will start the background process of retrieving the List of Highest Rated Movies, and then
     * populate Grid with the same list.
     */
    void loadHighestRatedMovies() {
        RetrofitApiInterface apiService =
                RetrofitApiClient.getClient(getActivity()).create(RetrofitApiInterface.class);

        Call<MovieResponse> call = apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (movies != null)
                    movies.clear();
                movies = response.body().getResults();
                gridMoviesAdapter = new GridMoviesAdapter(movies, TopRatedMoviesFragment.this,getActivity());
                recyclerView.setAdapter(gridMoviesAdapter);
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable throwable) {
                // Use the Logger since the response was not populated
                Log.e(TAG, throwable.toString());
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
                if (throwable.getMessage()!= null){
                    showWarning(throwable.getMessage());
                }
                else{
                    String errorMessage = getString(R.string.error_movies_not_loaded);
                    showWarning(errorMessage);
                }
            }
        });
    }

    @Override
    public void onCellClick(View view, int position) {
        String thumbnailURL, synopsis, movieReleaseDate, movieId, movieTitle, avgVoteCount, totalVoteCount;
        Movies selectedMovie;
        if (usingSavedInstance == 1) {
            selectedMovie = savedMovies.get(position);
        } else {
            selectedMovie = movies.get(position);
        }
        thumbnailURL = selectedMovie.getThumbnailURL();
        synopsis = selectedMovie.getSynopsis();
        movieReleaseDate = selectedMovie.getReleaseDate();
        movieId = selectedMovie.getMovieId();
        movieTitle = selectedMovie.getMovieTitle();
        avgVoteCount = selectedMovie.getAvgVoteCount();
        totalVoteCount = selectedMovie.getTotalVoteCount();

        MovieDetailsFragment.newInstance(thumbnailURL, synopsis, movieReleaseDate, movieId, movieTitle, avgVoteCount,totalVoteCount);
        onItemClickCallback.onMovieSelected(position);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (gridLayoutManager != null) {
            scrollPosition = gridLayoutManager.findFirstVisibleItemPosition();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (gridLayoutManager != null) {
            recyclerView.scrollToPosition(scrollPosition);
        }
    }

    private void showWarning(final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
