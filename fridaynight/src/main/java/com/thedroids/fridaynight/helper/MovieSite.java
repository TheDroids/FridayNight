package com.thedroids.fridaynight.helper;

/**
 * Movie Web Sites like IMDB, Rotten Tomatoes, etc.
 */
public enum MovieSite {
    Rotten_Tomatoes ("rotten_tomatoes"),
    IMDB            ("imdb"),
    The_Movie_DB    ("the_movie_db");

    private MovieSite(String siteName) {
        this.siteName = siteName;
    }

    private String siteName;
}
