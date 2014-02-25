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
import android.widget.ProgressBar;

import com.thedroids.fridaynight.R;
import com.thedroids.fridaynight.adapter.MovieAdapter;
import com.thedroids.fridaynight.model.Movie;
import com.thedroids.fridaynight.service.MovieService;

import java.util.ArrayList;
import java.util.List;


/**
 * This week's popular movies.
 *
 */
public class PopularMoviesFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular_movies, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mMoviesList   = new ArrayList<Movie>();
        mMovieAdapter = new MovieAdapter(getActivity(), mMoviesList);

        mProgressIndicator = (ProgressBar) getView().findViewById(R.id.pbLoading2);
        
        mMoviesListView = (ListView) getView().findViewById(R.id.lvPopularMovies);
        mMoviesListView.setEmptyView(mProgressIndicator);
        mMoviesListView.setAdapter(mMovieAdapter);

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
        Log.v("PopularMoviesFragment.onResume", "");

        MovieService.popular(getString(R.string.moviedb_api_key),
                new MovieService.Listener() {
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                    @Override
                    public void onResultAvailable(Object result) {
                        List<Movie> movies = (List<Movie>) result;
                        mMovieAdapter.clear();
                        mMovieAdapter.addAll(movies);
                    }
                });

        super.onResume();
    }

    ListView mMoviesListView;
    ProgressBar mProgressIndicator;

    List<Movie> mMoviesList;
    MovieAdapter mMovieAdapter;
}
