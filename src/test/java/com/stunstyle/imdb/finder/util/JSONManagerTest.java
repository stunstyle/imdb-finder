package com.stunstyle.imdb.finder.util;

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

            assertTrue(jm.getEpisodesOfSeriesSeason("Dexter", 7).equals(
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

    // TODO: ADD NEW TEST FOR GETMOVIETOKENS
}
