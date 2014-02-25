package com.thedroids.fridaynight.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.thedroids.fridaynight.R;
import com.thedroids.fridaynight.adapter.MovieAdapter;
import com.thedroids.fridaynight.model.Movie;
import com.thedroids.fridaynight.service.MovieService;

import java.util.ArrayList;
import java.util.List;

public class RecommendedMoviesFragment extends Fragment {
    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(android.os.Bundle)} and {@link #onActivityCreated(android.os.Bundle)}.
     * <p/>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommended_movies, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.  It is also useful for fragments that use
     * {@link #setRetainInstance(boolean)} to retain their instance,
     * as this callback tells the fragment when it is fully associated with
     * the new activity instance.  This is called after {@link #onCreateView}
     * and before {@link #onViewStateRestored(android.os.Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mMoviesList   = new ArrayList<Movie>();
        mMovieAdapter = new MovieAdapter(getActivity(), mMoviesList);

        ListView lvMovieList = (ListView) getView().findViewById(R.id.lvMovieList);
        lvMovieList.setAdapter(mMovieAdapter);

        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        Log.v("RecommendedMoviesFragment.onResume", "");
        super.onResume();

        MovieService.recommend(getString(R.string.moviedb_api_key),
                new MovieService.Listener() {
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                    @Override
                    public void onResultAvailable(Object result) {
                        List<Movie> movies = (List<Movie>) result;
                        mMovieAdapter.clear();
                        mMovieAdapter.addAll(movies);
                    }
        });
    }

    List<Movie> mMoviesList;
    MovieAdapter mMovieAdapter;
}
