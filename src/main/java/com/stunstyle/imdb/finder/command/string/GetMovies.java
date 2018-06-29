package com.stunstyle.imdb.finder.command.string;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.stunstyle.imdb.finder.util.*;

public class GetMovies implements StringCommand {
    private String command;

    public GetMovies(String command) {
        this.command = command;
    }

    @Override
    public String execute() {
        CommandParser commandParser = CommandParser.getCommandParser();
        String order = commandParser.getOrder(command);
        String[] genres = commandParser.getGenres(command);
        String[] actors = commandParser.getActors(command);
        Arrays.stream(actors).map(String::trim).toArray(unused -> actors);
        if (genres != null) {
            Arrays.stream(genres).map(String::trim).toArray(unused -> genres);
        }

        List<MovieRatingPair> allMoviePairs = FileManager.getFM().getAllCachedMoviePairs();
        Iterator<MovieRatingPair> it = allMoviePairs.iterator();
        while (it.hasNext()) {
            MovieRatingPair curr = it.next();
            if (!JSONManager.getJSONManager().movieHasTokens(curr.getMovieName(), actors, MovieToken.ACTORS)) {
                it.remove();
            }
        }

        if (genres != null) {
            it = allMoviePairs.iterator();
            while (it.hasNext()) {
                MovieRatingPair curr = it.next();
                if (!JSONManager.getJSONManager().movieHasTokens(curr.getMovieName(), genres, MovieToken.GENRE)) {
                    it.remove();
                }
            }
        }

        Collections.sort(allMoviePairs);
        if (order != null && order.equals("desc")) {
            Collections.reverse(allMoviePairs);
        }

        StringBuilder sb = new StringBuilder();
        for (MovieRatingPair pair : allMoviePairs) {
            sb.append(pair.getMovieName().replace("%20", " ") + ":" + pair.getMovieRating() + System.lineSeparator());
        }
        return sb.toString();
    }

}
