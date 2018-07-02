package com.stunstyle.imdb.finder;

import com.stunstyle.imdb.finder.command.file.GetPoster;
import com.stunstyle.imdb.finder.command.string.ClearCache;
import com.stunstyle.imdb.finder.command.string.GetMovie;
import com.stunstyle.imdb.finder.command.string.GetMovies;
import com.stunstyle.imdb.finder.command.string.GetTvSeries;
import com.stunstyle.imdb.finder.util.CommandParser;

import java.nio.charset.StandardCharsets;

public class IMDBCommandProcessor {
    private String command;

    public IMDBCommandProcessor(String command) {
        this.command = command;
    }

    public byte[] processCommand() {
        CommandParser commandParser = CommandParser.getCommandParser();
        if (!commandParser.commandIsValid(command)) {
            return "Invalid command!".getBytes(StandardCharsets.UTF_8);
        }

        if (commandParser.isStringCommand(command)) {
            return processStringCommand(command).getBytes(StandardCharsets.UTF_8);
        } else if (commandParser.isFileCommand(command)) {
            return processFileCommand(command);
        } else {
            // throw new UnsupportedOperationException("Unsupported command!");
            return "Unsupported command!".getBytes(StandardCharsets.UTF_8);
        }
    }

    public String processStringCommand(String command) {
        String commandName = CommandParser.getCommandParser().getCommandName(command);
        switch (commandName) {
            case "get-movie":
                return new GetMovie(command).execute();
            case "get-tv-series":
                return new GetTvSeries(command).execute();
            case "get-movies":
                return new GetMovies(command).execute();
            case "clear-cache":
                return new ClearCache().execute();
            default:
                return "Unsupported command!";
        }
    }

    public byte[] processFileCommand(String command) {
        String commandName = CommandParser.getCommandParser().getCommandName(command);
        switch (commandName) {
            case "get-movie-poster":
                return new GetPoster(command).execute();
            default:
                return "Unsupported command!".getBytes(StandardCharsets.UTF_8);
        }
    }
}