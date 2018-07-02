package com.stunstyle.imdb.finder.util;

import java.util.Set;

public class CommandParser {
    // responsible for all parsing of commands
    private static CommandParser commandParser = new CommandParser();
    private Set<String> stringCommands;
    private Set<String> fileCommands;

    private CommandParser() {
        stringCommands = Set.of("get-movie", "get-tv-series", "get-movies", "clear-cache");
        fileCommands = Set.of("get-movie-poster");
    }

    public static CommandParser getCommandParser() {
        return commandParser;
    }

    public String getCommandName(String command) {
        return command.split(" ")[0];
    }

    public boolean isStringCommand(String command) {
        return stringCommands.contains(getCommandName(command));
    }

    public boolean isFileCommand(String command) {
        return fileCommands.contains(getCommandName(command));
    }

    public String getMovieName(String command) {
        String[] commandParts = command.split(" ");
        String movieName = commandParts[1];
        for (int i = 2; i < commandParts.length; ++i) {
            if (commandParts[i].startsWith("--fields")) {
                break;
            }
            movieName = movieName + "%20" + commandParts[i];
        }
        return movieName;
    }

    public String getTvSeriesName(String command) {
        String[] commandParts = command.split(" ");
        String seriesName = commandParts[1];
        for (int i = 2; i < commandParts.length; ++i) {
            if (commandParts[i].startsWith("--season")) {
                break;
            }
            seriesName = seriesName + "%20" + commandParts[i];
        }
        return seriesName;
    }

    public int getSeason(String command) {
        String[] commandParts = command.split(" ");
        return Integer.parseInt(commandParts[commandParts.length - 1].split("=")[1]);
    }

    public String[] getFields(String command) {
        String[] commandParts = command.split(" ");
        int fieldsStart = -1;
        for (int i = 0; i < commandParts.length; ++i) {
            if (commandParts[i].startsWith("--fields")) {
                fieldsStart = i;
            }
        }
        if (fieldsStart == -1) {
            return null;
        }

        return commandParts[fieldsStart].split("=")[1].split(",");
    }

    public String getOrder(String command) {
        String[] commandParts = command.split(" ");
        for (String s : commandParts) {
            if (s.startsWith("--order=")) {
                return s.split("=")[1];
            }
        }
        return null;
    }

    public String[] getGenres(String command) {
        String[] commandParts = command.split(" ");
        for (String s : commandParts) {
            if (s.startsWith("--genres=")) {
                commandParts = s.split("=")[1].split(",");
                for (String str : commandParts) {
                    str = str.trim();
                    // TODO: with stream
                }
                return commandParts;
            }
        }
        return null;
    }

    public String[] getActors(String command) {
        String[] commandParts = command.split("--");
        for (String s : commandParts) {
            if (s.startsWith("actors=")) {
                commandParts = s.split("=")[1].split(",");
                for (String str : commandParts) {
                    str = str.trim();
                    // TODO: with stream
                }
                return commandParts;
            }
        }
        return null;
    }

    public boolean commandIsValid(String command) {
        switch (getCommandName(command)) {
            case "get-movie":
                return true;
            case "get-tv-series":

                return command.contains("--season=");

            case "get-movies":
                return command.contains("--actors=");

            case "get-movie-poster":
                return true;

            case "clear-cache":
                return true;
        }
        return false;
    }
}
