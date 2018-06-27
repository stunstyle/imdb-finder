package com.stunstyle.imdb.finder;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.stunstyle.imdb.finder.exception.MovieNotFoundException;
import com.stunstyle.imdb.finder.exception.SeriesOrSeasonNotFoundException;
import com.stunstyle.imdb.finder.util.FileManager;

public class FileManagerTest {
    private FileManager fileManager = FileManager.getFM();

    @Test
    public void downloadMovieTest() {
        try {
            fileManager.downloadMovie("The Shape Of Water");
        } catch (MovieNotFoundException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        File file = new File("cache", "The Shape Of Water.json");
        assertTrue(file.exists());
        file.delete();
    }

    @Test
    public void downloadSeriesSeasonTest() {
        try {
            fileManager.downloadSeriesSeason("Dexter", 8);
            File file = new File("cache", "Dexter_season_8.json");
            assertTrue(file.exists());
            file.delete();
        } catch (SeriesOrSeasonNotFoundException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void downloadPosterTest() {
        try {
            fileManager.downloadPoster("Scarface");
            File file = new File("cache//posters", "Scarface.jpg");
            assertTrue(file.exists());
            file.delete();
        } catch (MovieNotFoundException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
