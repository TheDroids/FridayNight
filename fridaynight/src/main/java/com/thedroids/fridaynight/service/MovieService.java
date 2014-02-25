package com.thedroids.fridaynight.service;

import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.thedroids.fridaynight.client.TheMovieDbClient;
import com.thedroids.fridaynight.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A web service wrapper for The Movie Database and other related services.
 *
 */
public class MovieService {
    public interface Listener {
        public void onResultAvailable(Object result);
    }

    private static final int MAX_RECOMMENDATIONS = 5;

    /**
     * Recommend movies for tonight.
     *
     * @param apiKey          The MovieDB API key
     * @param serviceListener A listener that gets called whenever results are available
     */
    public static void recommend(final String apiKey, final Listener serviceListener) {
        initClient(apiKey);

        if (sMovieList.isEmpty()) {
            sTheMovieDbClient.discoverMovies(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject response) {
                    Log.v("recommend", response.toString());
                    try {
                        sMovieList = Movie.fromJson(response.getJSONArray("results"));
                        /*
                        * The Movie DB API doesn't provide count filter, so we have to trim
                        * the list ourselves.
                        */
                        sMovieList = sMovieList.subList(0, MAX_RECOMMENDATIONS);
                        populateMovieDetails(sMovieList);
                        serviceListener.onResultAvailable(sMovieList);
                    } catch (JSONException e) {
                        Log.e("recommend", e.toString());
                    }
                }

                @Override
                public void onFailure(Throwable throwable, JSONArray jsonArray) {
                    Log.e("recommend", throwable.toString());
                    super.onFailure(throwable, jsonArray);
                }
            });
        } else {
            serviceListener.onResultAvailable(sMovieList);
        }
    }

    private static final int MAX_POPULAR = 10;

    /**
     * Popular movies for tonight.
     *
     * @param apiKey          The MovieDB API key
     * @param serviceListener A listener that gets called whenever results are available
     */
    public static void popular(final String apiKey, final Listener serviceListener) {
        initClient(apiKey);

        if (sPopularList.isEmpty()) {
            sTheMovieDbClient.popularMovies(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject response) {
                    Log.v("popular", response.toString());
                    try {
                        sPopularList = Movie.fromJson(response.getJSONArray("results"));
                        sPopularList = sPopularList.subList(0, MAX_POPULAR);
                        populateMovieDetails(sPopularList);
                        serviceListener.onResultAvailable(sPopularList);
                    } catch (JSONException e) {
                        Log.e("popular", e.toString());
                    }
                }

                @Override
                public void onFailure(Throwable throwable, JSONArray jsonArray) {
                    Log.e("popular", throwable.toString());
                    super.onFailure(throwable, jsonArray);
                }
            });
        } else {
            serviceListener.onResultAvailable(sPopularList);
        }
    }

    private static void populateMovieDetails(final List<Movie> movieList) {
        final int numMovies = movieList.size();
        final CountDownLatch latch = new CountDownLatch(numMovies);
        final ExecutorService executor = Executors.newFixedThreadPool(numMovies);
        Log.v("populateMovieDetails", "There are " + numMovies + " movies");

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                for (int i = 0; i < numMovies; i++) {
                    final int j = i;
                    Log.v("populateMovieDetails", j + "th call");
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            sTheMovieDbClient.getMovieDetail(
                                    movieList.get(j).getMovieId(Movie.Provider.The_Movie_DB),
                                    new JsonHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(JSONObject response) {
                                            Movie movie     = movieList.get(j);
                                            long latchCount = latch.getCount();

                                            movie.updateMovieDetails(response);
                                            movieList.set(j, movie);
                                            latch.countDown();
                                            Log.v("populateMovieDetails",
                                                    "Counting Down ..." + latchCount);
                                        }
                                    });
                        }
                    });
                }

                return null;
            }
        }.execute();

        try {
            Log.v("populateMovieDetails", "Awaiting ...");
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void initClient(final String apiKey) {
        if (sTheMovieDbClient == null) {
            sTheMovieDbClient = new TheMovieDbClient(apiKey);
        }
    }

    private MovieService() {}

    private static List<Movie> sPopularList = new ArrayList<Movie>();
    private static List<Movie> sMovieList = new ArrayList<Movie>();
    private static TheMovieDbClient sTheMovieDbClient;
}
