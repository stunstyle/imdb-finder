package com.stunstyle.imdb.finder.command.string;

import org.junit.Test;

import static org.junit.Assert.*;

public class GetTvSeriesTest {

    @Test
    public void GetTvSeriesSeasonTest() {
        StringCommand getTvSeries = new GetTvSeries("get-tv-series Dexter --season=2");
        String actualResult = getTvSeries.execute();
        String expectedResult = "\"It's Alive!\"\n" +
                "\"Waiting to Exhale\"\n" +
                "\"An Inconvenient Lie\"\n" +
                "\"See-Through\"\n" +
                "\"The Dark Defender\"\n" +
                "\"Dex, Lies, and Videotape\"\n" +
                "\"That Night, a Forest Grew\"\n" +
                "\"Morning Comes\"\n" +
                "\"Resistance Is Futile\"\n" +
                "\"There's Something About Harry\"\n" +
                "\"Left Turn Ahead\"\n" +
                "\"The British Invasion\"";
    }
}