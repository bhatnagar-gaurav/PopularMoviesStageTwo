package com.udacity.assignment.android.popularmoviesstagetwo.utilities.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.udacity.assignment.android.popularmoviesstagetwo.R;
import com.udacity.assignment.android.popularmoviesstagetwo.model.Movies;
import com.udacity.assignment.android.popularmoviesstagetwo.utilities.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link GridMoviesAdapter} exposes a list of movies to a
 * {@link android.support.v7.widget.RecyclerView}
 */

public class GridMoviesAdapter extends RecyclerView.Adapter<GridMoviesAdapter.MovieHolder> {
    private static final String TAG = GridMoviesAdapter.class.getSimpleName();

    private ArrayList<Movies> itemsMovies = null;
    private Context context;

    /*
    * An on-click handler that we've defined to make it easy for the Fragment to interface with
    * the RecyclerView
    */
    private GridAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */

    public interface GridAdapterOnClickHandler{
        void onCellClick(View view, int position);
    }

    /**
     * Creates a GridMoviesAdapter.
     *
     * @param moviesList The Array-List of Movies through which the RecyclerView has to be populated.
     * @param context  The Context of the Activity in which the RecyclerView will be shown.
     */

    public GridMoviesAdapter(ArrayList<Movies> moviesList, GridAdapterOnClickHandler onClickHandler, Context context) {
        itemsMovies = moviesList;
        this.mClickHandler = onClickHandler;
        this.context = context;
    }

    /**
     * This gets called when each new MovieHolder is created. This happens when the RecyclerView
     * is laid out. Enough MovieHolders(ViewHolders) will be created to fill the screen and allow for scrolling.
     *
     * @param parent The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new MovieHolder that holds the View for each grid item
     */
    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForGridItem = R.layout.movie_cell_card;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForGridItem,parent,shouldAttachToParentImmediately);
        return new MovieHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the data of the MovieHolder to display the correct
     * Image and Text in the Grid for this particular position, using the "position" argument that is conveniently
     * passed into this function.
     *
     * @param movieViewholder  ViewHolder(MovieHolder) which should be updated to represent the contents of the
     *                          item at the given position in the data set.
     * @param position          The position of the item within the adapter's data set.
     */

    @Override
    public void onBindViewHolder(final MovieHolder movieViewholder, int position) {
        Log.v(TAG,"Binding to the view #"+position);
       movieViewholder.bind(itemsMovies.get(position));
    }

    /**
     * This method simply returns the number of items to discoplay. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our movie Data-set.
     */
    @Override
    public int getItemCount() {
        return itemsMovies == null ? 0 : itemsMovies.size();
    }


    /**
     * Cache of the children views for a Grid item.
     */
    class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final String MOVIE_TAG = MovieHolder.class.getSimpleName();
        // This View is used as a reference to ItemView in the Constructor
        View view;
        // Used for rendering the Thumbnail for Each Movie in the Grid .
        @BindView(R.id.movie_thumbnail)
        ImageView movieThumbnail;
        // Used for Showing the Movie Title in Each Grid Cell.
        @BindView(R.id.item_movie_title)
        TextView movieTitle;
        // Used for Showing the Base for Each Movie Cell.
        @BindView(R.id.item_movie_base)
        LinearLayout movieBase;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextView and ImageView and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         * @param itemView The View that you inflated in
         *                 {@link GridMoviesAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        MovieHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.view = itemView;
            ButterKnife.bind(this, view);
        }

        /**
         * A method we wrote for convenience. This method will take Movie model as input and
         * use that data to display the appropriate image and the text within the grid cell.
         * @param movie Position of the movie in the Array List
         */
        void bind(Movies movie) {
            try {
                String specificThumbnailURI = movie.getThumbnailURL();
                String completeThumbnailURL = Constants.FINAL_THUMBNAIL_URI + specificThumbnailURI;
                Glide.clear(movieThumbnail);
                Glide.with(context)
                        .load(completeThumbnailURL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                Log.e(MOVIE_TAG, "Exception Occurred in Rendering Thumbnails");
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model,
                                                           Target<GlideDrawable> target,
                                                           boolean isFromMemoryCache, boolean isFirstResource) {
                                Bitmap bitmap = ((GlideBitmapDrawable) resource.getCurrent()).getBitmap();
                                Palette palette = Palette.from(bitmap).generate();
                                int defaultColor = ContextCompat.getColor(context,R.color.item_movie_base);
                                int color = palette.getDarkMutedColor(defaultColor);
                                movieBase.setBackgroundColor(color);
                                return false;
                            }
                        })
                        .into(movieThumbnail);
                movieTitle.setText(movie.getMovieTitle());
            } catch (Exception e) {
                Log.e(MOVIE_TAG, "Exception Occurred while populating UI");
            }
        }

        /**
         * This gets called by the child views during a click.
         * @param view The View that was clicked
         */

        @Override
        public void onClick(View view) {

            if (mClickHandler != null) {
                mClickHandler.onCellClick(view, getAdapterPosition());
            }
        }
    }


}
