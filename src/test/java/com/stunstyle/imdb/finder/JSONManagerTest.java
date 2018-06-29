package com.stunstyle.imdb.finder;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import com.stunstyle.imdb.finder.util.MovieToken;
import org.junit.Ignore;
import org.junit.Test;

import com.stunstyle.imdb.finder.exception.MovieNotFoundException;
import com.stunstyle.imdb.finder.exception.SeriesOrSeasonNotFoundException;
import com.stunstyle.imdb.finder.util.FileManager;
import com.stunstyle.imdb.finder.util.JSONManager;

public class JSONManagerTest {
    private JSONManager jm = JSONManager.getJSONManager();
    FileManager fm = FileManager.getFM();

    @Test
    public void getJSONFieldsTest() {

        if(!fm.movieExistsInCache("Titanic")) {
            try {
                fm.downloadMovie("Titanic");
            } catch (MovieNotFoundException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
        String[] fields = {"Rated"};
        assertTrue(jm.getJSONFields("Titanic", fields).trim().equals("Rated:\"PG-13\""));
    }

    @Test
    public void getEpisodesOfSeriesSeasonTest() {
        if(!fm.tvSeriesSeasonExistsInCache("Dexter", 7)) {
            try {
                fm.downloadSeriesSeason("Dexter", 7);
            } catch (SeriesOrSeasonNotFoundException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }

            assertTrue(jm.getEpisodesOfSeriesSeason("Dexter", 7).equals("List of episodes of Dexter, season 7\r\n" +
                    "Are You...?\r\n" +
                    "Sunshine and Frosty Swirl\r\n" +
                    "Buck the System\r\n" +
                    "Run\r\n" +
                    "Swim Deep\r\n" +
                    "Do the Wrong Thing\r\n" +
                    "Chemistry\r\n" +
                    "Argentina\r\n" +
                    "Helter Skelter\r\n" +
                    "The Dark... Whatever\r\n" +
                    "Do You See What I See?\r\n" +
                    "Surprise, Motherfucker!"));
        }

    }

    @Test
    public void getImdbIdFromLocalJsonTest() {
        assertTrue(jm.getImdbIdFromLocalJson("Titanic").equals("tt0120338"));
    }

    @Test
    public void getMovieActorsTest() {
        String[] movieActors = jm.getMovieActors("Titanic");
        String[] titanicActors = {"Leonardo DiCaprio",
                "Kate Winslet",
                "Billy Zane",
                "Kathy Bates"};
        Arrays.stream(movieActors).map(String::trim).toArray(unused -> movieActors);
        assertTrue(Arrays.asList(movieActors).equals(Arrays.asList(titanicActors)));
    }

    @Test
    public void getMovieGenresTest() {
        String[] movieGenres = jm.getMovieGenres("Titanic");
        String[] titanicGenres = {"Drama","Romance"};
        Arrays.stream(movieGenres).map(String::trim).toArray(unused -> movieGenres);
        assertTrue(Arrays.asList(movieGenres).equals(Arrays.asList(titanicGenres)));
    }

    @Test
    public void newJsonTest() {
        jm.getMovieTokens("Titanic", MovieToken.GENRE);
        jm.getMovieTokens("Titanic", MovieToken.ACTORS);
    }
}
