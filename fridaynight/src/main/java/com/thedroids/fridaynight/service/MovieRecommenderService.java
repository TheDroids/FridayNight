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
 * Recommend a movie.
 */
public class MovieRecommenderService {
    public interface Listener {
        public void onResultAvailable(Object result);
    }


    public static void recommend(final Listener serviceListener) {
        if (sMovieList.isEmpty()) {
            sTheMovieDbClient.discoverMovies(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject response) {
                    Log.v("recommend", response.toString());
                    try {
                        sMovieList = Movie.fromJson(response.getJSONArray("results"));
                        // TODO: testing with small payload
                        sMovieList = sMovieList.subList(0, 5);
                        populateMovieDetails();
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

    private static void populateMovieDetails() {
        final int numMovies = sMovieList.size();
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
                                    sMovieList.get(j).getMovieId(Movie.Provider.The_Movie_DB),
                                    new JsonHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(JSONObject response) {
                                            Movie movie     = sMovieList.get(j);
                                            long latchCount = latch.getCount();

                                            movie.updateMovieDetails(response);
                                            sMovieList.set(j, movie);
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

    private MovieRecommenderService() {}

    private static List<Movie> sMovieList = new ArrayList<Movie>();
    private static TheMovieDbClient sTheMovieDbClient = new TheMovieDbClient();
}
