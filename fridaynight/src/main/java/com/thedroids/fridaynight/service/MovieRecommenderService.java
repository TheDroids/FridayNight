package com.thedroids.fridaynight.service;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.thedroids.fridaynight.client.TheMovieDbClient;
import com.thedroids.fridaynight.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
                    Log.v("recommendOne", response.toString());
                    try {
                        sMovieList = Movie.fromJson(response.getJSONArray("results"));
                        serviceListener.onResultAvailable(sMovieList);
                    } catch (JSONException e) {
                        Log.e("recommendOne", e.toString());
                    }
                }

                @Override
                public void onFailure(Throwable throwable, JSONArray jsonArray) {
                    Log.e("recommendOne", throwable.toString());
                    super.onFailure(throwable, jsonArray);
                }
            });
        } else {
            serviceListener.onResultAvailable(sMovieList);
        }
    }

    private MovieRecommenderService() {}

    private static List<Movie> sMovieList = new ArrayList<Movie>();
    private static TheMovieDbClient sTheMovieDbClient = new TheMovieDbClient();
}
