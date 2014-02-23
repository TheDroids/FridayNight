package com.thedroids.fridaynight.model;

import com.thedroids.fridaynight.client.TheMovieDbClient;
import com.thedroids.fridaynight.helper.MovieSite;

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
    public static Movie fromJson(final JSONObject jsonObject) {
        Movie movie = new Movie();

        try {
            movie.setTitle(jsonObject.getString("title"));
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            movie.setReleaseDates(new HashMap<String, Date>() {
                {
                    put("theatre", dateFormat.parse(jsonObject.getString("release_date")));
                }
            });
            String posterImage = jsonObject.getString("poster_path");
            movie.setPosterOriginal(TheMovieDbClient.getImageUrl(posterImage,
                    TheMovieDbClient.PosterSize.MEDIUM));
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

    public Map<MovieSite, String> getMovieId() {
        return movieId;
    }

    public void setMovieId(Map<MovieSite, String> movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getMpaaRating() {
        return mpaaRating;
    }

    public void setMpaaRating(String mpaaRating) {
        this.mpaaRating = mpaaRating;
    }

    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    public String getCriticsConsensus() {
        return criticsConsensus;
    }

    public void setCriticsConsensus(String criticsConsensus) {
        this.criticsConsensus = criticsConsensus;
    }

    public Map<String, Date> getReleaseDates() {
        return releaseDates;
    }

    public void setReleaseDates(Map<String, Date> releaseDates) {
        this.releaseDates = releaseDates;
    }

    public String getCriticsRating() {
        return criticsRating;
    }

    public void setCriticsRating(String criticsRating) {
        this.criticsRating = criticsRating;
    }

    public int getCriticsScore() {
        return criticsScore;
    }

    public void setCriticsScore(int criticsScore) {
        this.criticsScore = criticsScore;
    }

    public String getAudienceRating() {
        return audienceRating;
    }

    public void setAudienceRating(String audienceRating) {
        this.audienceRating = audienceRating;
    }

    public int getAudienceScore() {
        return audienceScore;
    }

    public void setAudienceScore(int audienceScore) {
        this.audienceScore = audienceScore;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getPosterThumbnail() {
        return posterThumbnail;
    }

    public void setPosterThumbnail(String posterThumbnail) {
        this.posterThumbnail = posterThumbnail;
    }

    public String getPosterProfile() {
        return posterProfile;
    }

    public void setPosterProfile(String posterProfile) {
        this.posterProfile = posterProfile;
    }

    public String getPosterDetailed() {
        return posterDetailed;
    }

    public void setPosterDetailed(String posterDetailed) {
        this.posterDetailed = posterDetailed;
    }

    public String getPosterOriginal() {
        return posterOriginal;
    }

    public void setPosterOriginal(String posterOriginal) {
        this.posterOriginal = posterOriginal;
    }

    public List<Map<String, Object>> getAbridgedCast() {
        return abridgedCast;
    }

    public void setAbridgedCast(List<Map<String, Object>> abridgedCast) {
        this.abridgedCast = abridgedCast;
    }

    public List<Map<String, String>> getAbridgedDirectors() {
        return abridgedDirectors;
    }

    public void setAbridgedDirectors(List<Map<String, String>> abridgedDirectors) {
        this.abridgedDirectors = abridgedDirectors;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
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
    private Map<MovieSite, String> movieId;

    /**
     * Movie Title
     *
     * e.g., "Toy Story 3"
     */
    private String title;

    /**
     * Year of release
     *
     * e.g., 2010
     */
    private int year;

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
    private List<String> genres;

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
    private Map<String, Date> releaseDates;

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
    private List<Map<String, Object>> abridgedCast;

    /**
     * List of Directors
     *
     * e.g., [{"name": "Lee Unkrich"}]
     */
    private List<Map<String, String>> abridgedDirectors;

    /**
     * The production studio
     *
     * e.g., "Walt Disney Pictures"
     */
    private String studio;
}
