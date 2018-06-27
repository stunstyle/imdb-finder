package com.stunstyle.imdb.finder;

import static org.junit.Assert.*;

import java.net.URL;

import org.junit.Test;

import com.stunstyle.imdb.finder.util.URLBuilder;

public class URLBuilderTest {

    @Test
    public void buildOmdbMovieQueryTest() {
        URL testURL = URLBuilder.getBuilder().buildOmdbMovieQuery("Titanic");
        assertTrue(testURL.toString().equals("http://www.omdbapi.com/?apikey=f1cbdaae&t=Titanic"));
    }

    @Test
    public void buildOmdbSeriesSeasonQueryTest() {
        URL testURL = URLBuilder.getBuilder().buildOmdbSeriesSeasonQuery("Game Of Thrones", 3);
        assertTrue(testURL.toString()
                .equals("http://www.omdbapi.com/?apikey=f1cbdaae&t=Game Of Thrones&type=Series&Season=3"));
    }

    @Test
    public void buildTmdbFindQueryTest() {
        URL testURL = URLBuilder.getBuilder().buildTmdbFindQuery("tt0120338");
        assertTrue(testURL.toString().equals(
                "https://api.themoviedb.org/3/find/tt0120338?api_key=94f30d8b5d99be4d9504f319690c87bd&external_source=imdb_id"));
    }

    @Test
    public void buildTmdbConfigurationQueryTest() {
        URL testURL = URLBuilder.getBuilder().buildTmdbConfigurationQuery();
        assertTrue(testURL.toString()
                .equals("https://api.themoviedb.org/3/configuration?api_key=94f30d8b5d99be4d9504f319690c87bd"));
    }

    @Test
    public void buildFinalURLForPosterTest() {
        URL testURL = URLBuilder.getBuilder().buildFinalURLForPoster("https://image.tmdb.org/t/p/",
                "/kqjL17yufvn9OVLyXYpvtyrFfak.jpg");
        assertTrue(testURL.toString().equals("https://image.tmdb.org/t/p/w500/kqjL17yufvn9OVLyXYpvtyrFfak.jpg"));
    }

}
