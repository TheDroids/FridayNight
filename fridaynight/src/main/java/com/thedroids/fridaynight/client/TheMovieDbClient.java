package com.thedroids.fridaynight.client;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * The Movie Database client.
 */
public class TheMovieDbClient {
    public TheMovieDbClient() {
        this.mAsyncHttpClient = new AsyncHttpClient();
    }

    /**
     * Discover movies by different types of data like average rating, number of votes, genres and
     * certifications. You can get a valid list of certifications from the /certifications method.
     *
     * @param responseHandler The JSON response handler
     */
    public void discoverMovies(final JsonHttpResponseHandler responseHandler) {
        if (mConfiguration == null) {
            getConfiguration(new AsyncTaskListener() {
                @Override
                public void onTaskComplete() {
                    String url = getApiUrl("3/discover/movie");
                    RequestParams params = new RequestParams(new HashMap<String, String>() {
                        {
                            put("api_key", API_KEY);
                            put("language", "en");
                            put("include_adult", "false");
                            put("sort_by", "popularity.desc");
                        }
                    });
                    mAsyncHttpClient.get(url, params, responseHandler);
                }
            });
        }
    }

    /**
     * Get the basic movie information for a specific movie id.
     *
     * @param movieId           The MovieDB ID of the movie
     * @param responseHandler   The JSON response handler
     */
    public void getMovieDetail(final String movieId,
                               final JsonHttpResponseHandler responseHandler) {
        Log.v("getMovieDetail", "Fetching detail for " + movieId);
        String url = getApiUrl("3/movie/" + movieId);
        RequestParams params = new RequestParams(new HashMap<String, String>() {
            {
                put("api_key", API_KEY);
            }
        });
        mAsyncHttpClient.get(url, params, responseHandler);
    }

    interface AsyncTaskListener {
        public void onTaskComplete();
    }

    protected void getConfiguration(final AsyncTaskListener listener) {
        String url = getApiUrl("3/configuration");
        RequestParams params = new RequestParams(new HashMap<String, String>() {
            {
                put("api_key", API_KEY);
            }
        });
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.v("getConfiguration", response.toString());
                try {
                    final JSONObject images = response.getJSONObject("images");
                    final JSONArray sizes   = images.getJSONArray("poster_sizes");
                    final int numSizes      = sizes.length();
                    mConfiguration = new HashMap<String, String>() {
                        {
                            put("base_url", images.getString("base_url"));
                            put("poster_small_size", (String) sizes.get(0));
                            put("poster_medium_size", (String) sizes.get(numSizes / 2));
                            put("poster_large_size", (String) sizes.get(numSizes - 1));
                        }
                    };
                    listener.onTaskComplete();
                } catch (JSONException e) {
                    mConfiguration = null;
                    Log.e("getConfiguration", e.toString());
                }
            }

            @Override
            public void onFailure(Throwable throwable, JSONObject jsonObject) {
                super.onFailure(throwable, jsonObject);
                Log.e("getConfiguration", throwable.toString());
            }
        });
    }

    public enum PosterSize {
        SMALL  ("small"),
        MEDIUM ("medium"),
        LARGE  ("large");

        PosterSize(final String posterSize) {
            mPosterSize = posterSize;
        }

        public String getSize() {
            return mPosterSize;
        }

        private String mPosterSize;
    }

    public static String getImageUrl(String path, PosterSize size) {
        if (mConfiguration != null) {
            return mConfiguration.get("base_url") +
                    mConfiguration.get("poster_" + size.getSize() + "_size") + path;
        }

        return null;
    }

    private static String getApiUrl(String path) {
        return API_BASE_URL + path;
    }

    private static Map<String, String> mConfiguration;
    private AsyncHttpClient mAsyncHttpClient;

    private final String API_KEY = "2694dc7fbcf525196f0386adf2bce34a";
    private static final String API_BASE_URL = "http://api.themoviedb.org/";
}
