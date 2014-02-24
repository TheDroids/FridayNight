package com.thedroids.fridaynight.model;

import com.thedroids.fridaynight.client.TheMovieDbClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Movie model class.
 */
public class Movie {
    public enum Provider {
        Rotten_Tomatoes ("rotten_tomatoes"),
        IMDB            ("imdb"),
        The_Movie_DB    ("the_movie_db");

        private Provider(String siteName) {
            this.siteName = siteName;
        }

        private String siteName;
    }

    public static Movie fromJson(final JSONObject movieDbResult) {
        Movie movie = new Movie();

        try {
            movie.movieId.put(Provider.The_Movie_DB, String.valueOf(movieDbResult.getInt("id")));
            movie.title = movieDbResult.getString("title");
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            movie.releaseDates = new HashMap<String, Date>() {
                {
                    put("theatre", dateFormat.parse(movieDbResult.getString("release_date")));
                }
            };
            String posterImage = movieDbResult.getString("poster_path");
            movie.posterOriginal = TheMovieDbClient.getImageUrl(posterImage,
                    TheMovieDbClient.PosterSize.MEDIUM);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return movie;
    }

    public static List<Movie> fromJson(JSONArray jsonArray) {
        int i, len;
        List<Movie> movieList = new ArrayList<Movie>();

        for (i = 0, len = jsonArray.length(); i < len; i++) {
            try {
                Movie movie = Movie.fromJson((JSONObject) jsonArray.get(i));
                movieList.add(movie);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return movieList;
    }

    public void updateMovieDetails(JSONObject jsonObject) {
        try {
            JSONArray genres = jsonObject.getJSONArray("genres");
            for (int i = 0; i < genres.length(); i++) {
                String genre = genres.getJSONObject(i).getString("name");
                this.genres.add(genre);
            }
            this.synopsis = jsonObject.getString("overview");
            JSONArray studios = jsonObject.getJSONArray("production_companies");
            for (int i = 0; i < studios.length(); i++) {
                String studio = studios.getJSONObject(i).getString("name");
                this.productionCompanies.add(studio);
            }
            this.runTime = jsonObject.getInt("runtime");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Map<Provider, String> getMovieId() {
        return movieId;
    }

    public String getMovieId(Provider provider) {
        return movieId.get(provider);
    }

    public String getTitle() {
        return title;
    }

    public List<String> getGenres() {
        return genres;
    }

    public String getMpaaRating() {
        return mpaaRating;
    }

    public int getRunTime() {
        return runTime;
    }

    public String getCriticsConsensus() {
        return criticsConsensus;
    }

    public Map<String, Date> getReleaseDates() {
        return releaseDates;
    }

    public String getCriticsRating() {
        return criticsRating;
    }

    public int getCriticsScore() {
        return criticsScore;
    }

    public String getAudienceRating() {
        return audienceRating;
    }

    public int getAudienceScore() {
        return audienceScore;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getPosterThumbnail() {
        return posterThumbnail;
    }

    public String getPosterProfile() {
        return posterProfile;
    }

    public String getPosterDetailed() {
        return posterDetailed;
    }

    public String getPosterOriginal() {
        return posterOriginal;
    }

    public List<Map<String, Object>> getAbridgedCast() {
        return abridgedCast;
    }

    public List<String> getAbridgedDirectors() {
        return abridgedDirectors;
    }

    public List<String> getProductionCompanies() {
        return productionCompanies;
    }

    /**
     * Movie Ids from various systems
     *
     * e.g.,
     * {
     *   Rotten_Tomatoes: "770672122",
     *   IMDB: "9892181399"
     * }
     */
    private Map<Provider, String> movieId = new HashMap<Provider, String>();

    /**
     * Movie Title
     *
     * e.g., "Toy Story 3"
     */
    private String title;

    /**
     * Movie Genre
     *
     * e.g.,
     * [
     *   "Animation",
     *   "Kids & Family",
     *   "Science Fiction & Fantasy",
     *   "Comedy"
     * ]
     */
    private List<String> genres = new ArrayList<String>();

    /**
     * MPAA rating
     *
     * e.g., "G"
     */
    private String mpaaRating;

    /**
     * Running time in minutes
     *
     * e.g., 103
     */
    private int runTime;

    /**
     * Critics consensus
     *
     * e.g., "Deftly blending comedy, adventure, and honest emotion, Toy Story 3 is a rare second
     * sequel that really works."
     */
    private String criticsConsensus;

    /**
     * Release dates (in theatre, DVD, etc.)
     *
     * e.g.,
     * {
     *   "theater": "2010-06-18",
     *  "dvd": "2010-11-02"
     * }
     */
    private Map<String, Date> releaseDates = new HashMap<String, Date>();

    /**
     * Critics rating
     *
     * e.g., "Certified Fresh"
     */
    private String criticsRating;

    /**
     * Critics score
     *
     * e.g., 99
     */
    private int criticsScore;

    /**
     * Audience rating
     *
     * e.g., "Upright"
     */
    private String audienceRating;

    /**
     * Audience score
     *
     * e.g., 91
     */
    private int audienceScore;

    /**
     * Movie synopsis
     *
     * e.g., "Pixar returns to their first success with Toy Story 3. The movie begins with Andy
     * leaving for college and donating his beloved toys -- including Woody (Tom Hanks) and Buzz
     * (Tim Allen) -- to a daycare. While the crew meets new friends, including
     * Ken (Michael Keaton), they soon grow to hate their new surroundings and plan an escape.
     * The film was directed by Lee Unkrich from a script co-authored by Little Miss Sunshine
     * scribe Michael Arndt. ~ Perry Seibert, Rovi"
     */
    private String synopsis;

    /**
     * URL to the thumbnail poster
     *
     * e.g., "http://content6.flixster.com/movie/11/13/43/11134356_mob.jpg"
     */
    private String posterThumbnail;

    /**
     * URL to the profile poster
     *
     * e.g., "http://content6.flixster.com/movie/11/13/43/11134356_pro.jpg"
     */
    private String posterProfile;

    /**
     * URL to the detailed poster
     *
     * e.g., "http://content6.flixster.com/movie/11/13/43/11134356_det.jpg"
     */
    private String posterDetailed;

    /**
     * URL to the original poster
     *
     * e.g., "http://content6.flixster.com/movie/11/13/43/11134356_ori.jpg"
     */
    private String posterOriginal;

    /**
     * Cast information along with character.
     *
     * For multiple characters, the value will be separated by a comma (,)
     *
     * e.g.,
     * [
     *   {
     *      "name": "Tom Hanks",
     *      "characters": ["Woody"]
     *   },
     *   {
     *      "name": "Tim Allen",
     *      "characters": ["Buzz Lightyear"]
     *   },
     *   {
     *      "name": "Joan Cusack",
     *      "characters": ["Jessie the Cowgirl"]
     *   },
     *   {
     *      "name": "Don Rickles",
     *      "characters": ["Mr. Potato Head"]
     *   },
     *   {
     *      "name": "Wallace Shawn",
     *      "characters": ["Rex"]
     *   }
     * ]
     */
    private List<Map<String, Object>> abridgedCast = new ArrayList<Map<String, Object>>();

    /**
     * List of Directors
     *
     * e.g., ["Lee Unkrich"]
     */
    private List<String> abridgedDirectors = new ArrayList<String>();

    /**
     * The production companies
     *
     * e.g., ["Walt Disney Pictures"]
     */
    private List<String> productionCompanies = new ArrayList<String>();
}
