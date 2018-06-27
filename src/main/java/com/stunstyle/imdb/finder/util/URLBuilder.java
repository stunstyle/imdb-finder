package com.stunstyle.imdb.finder.util;

import java.net.MalformedURLException;
import java.net.URL;

public class URLBuilder {
    // responsible for building all URLs and queries
    private static final URLBuilder builder = new URLBuilder();
    private final String OMDB_API_KEY;
    private final String TMDB_API_KEY;
    private final String OMDB_BASE_URL;
    private final String TMDB_BASE_URL;
    private final String TMDB_POSTER_SIZE;

    private URLBuilder() {
        // responsible for all URL Building
        OMDB_API_KEY = "f1cbdaae";
        TMDB_API_KEY = "94f30d8b5d99be4d9504f319690c87bd";
        OMDB_BASE_URL = "http://www.omdbapi.com/?apikey=";
        TMDB_BASE_URL = "https://api.themoviedb.org/3/";
        TMDB_POSTER_SIZE = "w500";
    }

    public static URLBuilder getBuilder() {
        return builder;
    }

    public URL buildOmdbMovieQuery(String movieName) {
        String urlString = OMDB_BASE_URL + OMDB_API_KEY + "&t=" + movieName.replace(" ", "%20");
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            System.err.println("Unable to form URL!");
            e.printStackTrace();
        }
        return url;
    }

    public URL buildOmdbSeriesSeasonQuery(String seriesName, int season) {
        String urlString = OMDB_BASE_URL + OMDB_API_KEY + "&t=" + seriesName + "&type=Series" + "&Season=" + season;
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            System.err.println("Unable to form URL!");
            e.printStackTrace();
        }
        return url;
    }

    public URL buildTmdbFindQuery(String imdbID) {
        String urlString = TMDB_BASE_URL + "find/" + imdbID + "?api_key=" + TMDB_API_KEY + "&external_source=imdb_id";
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            System.err.println("Unable to form URL!");
            e.printStackTrace();
        }
        return url;

    }

    public URL buildTmdbConfigurationQuery() {
        String urlString = TMDB_BASE_URL + "configuration" + "?api_key=" + TMDB_API_KEY;
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            System.err.println("Can't form URL!");
            e.printStackTrace();
        }
        return url;

    }

    public URL buildFinalURLForPoster(String baseURL, String poster_path) {
        URL finalURL = null;
        try {
            finalURL = new URL(baseURL + TMDB_POSTER_SIZE + poster_path);
        } catch (MalformedURLException e) {
            System.err.println("Could not build final URL!");
            e.printStackTrace();
        }
        return finalURL;
    }
}
