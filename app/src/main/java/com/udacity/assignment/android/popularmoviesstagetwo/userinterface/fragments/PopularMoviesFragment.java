package com.udacity.assignment.android.popularmoviesstagetwo.userinterface.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.udacity.assignment.android.popularmoviesstagetwo.R;
import com.udacity.assignment.android.popularmoviesstagetwo.model.MovieResponse;
import com.udacity.assignment.android.popularmoviesstagetwo.model.Movies;
import com.udacity.assignment.android.popularmoviesstagetwo.utilities.Constants;
import com.udacity.assignment.android.popularmoviesstagetwo.utilities.adapters.GridMoviesAdapter;
import com.udacity.assignment.android.popularmoviesstagetwo.utilities.itemdecoration.ItemDecoration;
import com.udacity.assignment.android.popularmoviesstagetwo.utilities.networkutils.NetworkUtilFunctions;
import com.udacity.assignment.android.popularmoviesstagetwo.utilities.networkutils.RetrofitApiClient;
import com.udacity.assignment.android.popularmoviesstagetwo.utilities.networkutils.RetrofitApiInterface;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.udacity.assignment.android.popularmoviesstagetwo.utilities.Constants.API_KEY;


public class PopularMoviesFragment extends Fragment implements GridMoviesAdapter.GridAdapterOnClickHandler{

    private static final String TAG = PopularMoviesFragment.class.getSimpleName();

    private static final String BUNDLE_MOVIES = "BUNDLE_MOVIES";
    GridLayoutManager gridLayoutManager;
    GridMoviesAdapter gridMoviesAdapter;
    OnMovieItemClickListener onItemClickCallback;
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

    public PopularMoviesFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View popularMovieGrid = inflater.inflate(R.layout.popular_movies_list, container, false);
        ButterKnife.bind(this, popularMovieGrid);

        errorView.setVisibility(View.GONE);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.orange_swipe_refresh),
                ContextCompat.getColor(getActivity(),R.color.green_swipe_refresh),
                ContextCompat.getColor(getActivity(),R.color.cyan_swipe_refresh));

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            gridLayoutManager = new GridLayoutManager(getActivity(), Constants.PORTRAIT_GRID);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            gridLayoutManager = new GridLayoutManager(getActivity(), Constants.LANDSCAPE_GRID);
            recyclerView.setLayoutManager(gridLayoutManager);
        }

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        recyclerView.setHasFixedSize(true);


        recyclerView.addItemDecoration(new ItemDecoration(1, 1, false));

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            savedMovies = savedInstanceState.getParcelableArrayList(BUNDLE_MOVIES);
            gridMoviesAdapter = new GridMoviesAdapter(savedMovies,this,getActivity());
            recyclerView.setAdapter(gridMoviesAdapter);
            usingSavedInstance = 1;
        } else {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    if (NetworkUtilFunctions.isConnected(getActivity())) {
                        /* Enable the Refresh for swipeLayout  */
                        swipeRefreshLayout.setRefreshing(true);
                        showPopularMoviesGrid();
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
                    showPopularMoviesGrid();
                } else {
                    showErrorMessage();
                    Toast.makeText(getActivity(), getString(R.string.error_no_internet_sub_title), Toast.LENGTH_SHORT).show();
                }

            }
        });

        return popularMovieGrid;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (usingSavedInstance == 1) {
            savedInstanceState.putParcelableArrayList(BUNDLE_MOVIES, savedMovies);
        } else {
            savedInstanceState.putParcelableArrayList(BUNDLE_MOVIES, movies);
        }
    }


    /**
     * This method will make the RecyclerView for Popular Movies visible and
     * hide the error message.
     */
    private void showPopularMoviesGrid() {
        /* First, We have to make sure the error is invisible and the Grid is visible */
        errorView.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);

        /* Then, make sure the Grid of Popular Movies is visible */
        loadMostPopularMovies();
    }

    /**
     * This method will make the error message visible and hide the Grid of Popular Movies
     */
    private void showErrorMessage() {
        /* Disable the refresh if swipeRefresh is enabled */
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
        /* First, hide the Grid of Popular Movies */
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
                gridMoviesAdapter = new GridMoviesAdapter(movies,PopularMoviesFragment.this,getActivity());
                recyclerView.setAdapter(gridMoviesAdapter);
                errorView.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable throwable) {
                // Log error here since request failed
                Log.e(TAG, throwable.getMessage());
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

    /**
     * This method will start the background process of retrieving the List of Popular Movies, and then
     * populate the Grid with the same list.
     */
    void loadMostPopularMovies() {
        RetrofitApiInterface apiService =
                RetrofitApiClient.getClient(getActivity()).create(RetrofitApiInterface.class);

        Call<MovieResponse> call = apiService.getPopularMovies(API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                movies = response.body().getResults();
                gridMoviesAdapter = new GridMoviesAdapter(movies, PopularMoviesFragment.this,getActivity());
                recyclerView.setAdapter(gridMoviesAdapter);
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, "Number of movies received: " + movies.size());
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable throwable) {
                // Log error since the request failed
                Log.e(TAG, throwable.toString());
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
    public void onAttach(Activity accessActivity) {
        super.onAttach(accessActivity);

        try {
            onItemClickCallback = (OnMovieItemClickListener) accessActivity;
        } catch (ClassCastException e) {
            Log.d(TAG,e.toString());
            throw new ClassCastException(accessActivity.toString() + getString(R.string.fragment_attachment_exception));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.menu_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        switch (id) {
            case R.id.action_by_popular_sort:
                if (tabletSize) {
                    loadMostPopularMovies();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.menu_options_popular_message), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_by_top_rating_sort:
                if (tabletSize) {
                    loadHighestRatedMovies();
                } else {
                    TopRatedMoviesFragment fragment = new TopRatedMoviesFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content, fragment).commit();
                }
                return true;

        }
        return false;
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


    public interface OnMovieItemClickListener {
        void onMovieSelected(int position);
    }


}
