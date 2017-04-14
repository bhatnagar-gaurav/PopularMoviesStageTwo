package com.udacity.assignment.android.popularmoviesstageone.userinterface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.assignment.android.popularmoviesstageone.R;
import com.udacity.assignment.android.popularmoviesstageone.userinterface.fragments.MovieDetailsFragment;
import com.udacity.assignment.android.popularmoviesstageone.userinterface.fragments.PopularMoviesFragment;
import com.udacity.assignment.android.popularmoviesstageone.utilities.Constants;

public class AccessActivity extends AppCompatActivity implements  PopularMoviesFragment.OnMovieItemClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);
        if (findViewById(R.id.content) != null) {
            if (savedInstanceState != null) {
                return;
            }
            // Create an instance of Popular Movies Fragment which encapsulates the UI for the Grid of Popular Movies
            PopularMoviesFragment  popularMoviesFragment = new PopularMoviesFragment();

           /* *** Specify the Support Fragment Manager to Add the Fragment(View) for the Popular Movies in the
            ******* the Access Activity's layout.************************************************* */
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, popularMoviesFragment)
                    .commit();
        }
    }

    @Override
    public void onMovieSelected(int position) {
        /*To Show the specific movie's detail fragment from this activity's dual-pane layout */
        MovieDetailsFragment detailsFragment = (MovieDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.detailsfragment);
        /* If the DetailsFragment is null we need to create the same */
        if (detailsFragment != null) {
            /* In Case We are in two pane layout*/
            detailsFragment.updateMovieDetailsView(position);
        } else {
            /* ********************Currently we must be in one pane layout
            *************Create fragment for the movie detail and provide the argument for the position of selected movie */
            MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
            Bundle args = new Bundle();
            args.putInt(Constants.ARG_SELECTED_DETAIL, position);
            movieDetailsFragment.setArguments(args);

              /* To Specify the Support Fragment Manager to Add the View for the Movie Details in the
            ******* this Activity's layout. */

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, movieDetailsFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
