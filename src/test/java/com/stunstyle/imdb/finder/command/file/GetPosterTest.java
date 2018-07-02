package com.stunstyle.imdb.finder.command.file;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class GetPosterTest {
    @Test
    public void getPosterTest() {
        FileCommand getPoster = new GetPoster("get-movie-poster Scarface");
        getPoster.execute();
        File poster = new File("cache//posters//", "Scarface.jpg");
        assertTrue("poster should exist in cache", poster.exists());
        poster.delete();
    }
}