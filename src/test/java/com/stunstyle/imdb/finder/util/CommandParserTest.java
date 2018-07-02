package com.stunstyle.imdb.finder.util;

import com.stunstyle.imdb.finder.util.CommandParser;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CommandParserTest {

    @Test
    public void getCommandNameTest() {
        CommandParser commandParser = CommandParser.getCommandParser();
        assertTrue("this command's name should be get-movie",
                commandParser.getCommandName("get-movie Titanic").equals("get-movie"));
        assertTrue("this command's name should be get-movie",
                commandParser.getCommandName("get-movie The Revenant --fields=Genre,Actors").equals("get-movie"));
        assertTrue("this command's name should be get-tv-series",
                commandParser.getCommandName("get-tv-series Dexter --season=5").equals("get-tv-series"));
        assertTrue("this command's name should be get-tv-series",
                commandParser.getCommandName("get-tv-series Rick And Morty --season=2").equals("get-tv-series"));
    }

    @Test
    public void isStringCommandTest() {
        CommandParser commandParser = CommandParser.getCommandParser();
        assertTrue("get-movies should be a string command", commandParser.isStringCommand("get-movies"));
    }

    @Test
    public void isFileCommandTest() {
        CommandParser commandParser = CommandParser.getCommandParser();
        assertTrue("get-movie-poster should be a file command", commandParser.isFileCommand("get-movie-poster"));
    }

    @Test
    public void getMovieNameTest() {
        CommandParser commandParser = CommandParser.getCommandParser();
        assertTrue("movie name should be 12%20Strong",
                commandParser.getMovieName("get-movie 12 Strong").equals("12%20Strong"));
    }

    @Test
    public void getTvSeriesNameTest() {
        CommandParser commandParser = CommandParser.getCommandParser();
        assertTrue("series name should be Dexter",
                commandParser.getTvSeriesName("get-tv-series Dexter").equals("Dexter"));

    }

    @Test
    public void getSeasonTest() {
        CommandParser commandParser = CommandParser.getCommandParser();
        assertTrue("this query's season should be 7", commandParser.getSeason("get-tv-series Dexter --season=7") == 7);
    }

    @Test
    public void getFieldsTest() {
        CommandParser commandParser = CommandParser.getCommandParser();
        String[] fields = {"Website", "Rated", "imdbRating"};
        assertTrue(
                Arrays.equals(fields, commandParser.getFields("get-movie Titanic --fields=Website,Rated,imdbRating")));
        assertNull("should return null as there are no fields in this command", commandParser.getFields("get-movie Titanic"));

    }

    @Test
    public void getOrderTest() {
        CommandParser commandParser = CommandParser.getCommandParser();
        assertTrue(commandParser.getOrder("get-movies --order=asc").equals("asc"));
        assertTrue(commandParser.getOrder("get-movies --genres=Horror,Comedy --order=desc").equals("desc"));
    }

    @Test
    public void getGenresTest() {
        CommandParser commandParser = CommandParser.getCommandParser();
        String[] genres = {"Action", "Comedy", "Thriller"};
        assertTrue(Arrays.equals(genres,
                commandParser.getGenres("get-movies --order=asc --genres=Action,Comedy,Thriller")));
        assertNull(commandParser.getGenres("get-movies --actors=Leonardo Di Caprio"));
    }

    @Test
    public void getActorsTest() {
        CommandParser commandParser = CommandParser.getCommandParser();
        String[] actors = {"Leonardo Di Caprio", "Kit Harrington", "Al Pacino"};
        assertTrue(Arrays.equals(actors, commandParser
                .getActors("get-movies --order=asc --actors=Leonardo Di Caprio,Kit Harrington,Al Pacino")));
    }

    @Test
    public void commandIsValidTest() {
        CommandParser commandParser = CommandParser.getCommandParser();
        assertTrue("get-movie should be valid", commandParser.commandIsValid("get-movie The Incredibles"));
        assertFalse("random letters should be invalid", commandParser.commandIsValid("fdsczxsd sdlflxv fsdfs"));
        assertTrue("clear-cache should be valid", commandParser.commandIsValid("clear-cache"));
        assertFalse("empty command should be invalid", commandParser.commandIsValid(""));
        assertTrue("get-movie-poster should be valid", commandParser.commandIsValid("get-movie-poster The Godfather"));

    }

}
