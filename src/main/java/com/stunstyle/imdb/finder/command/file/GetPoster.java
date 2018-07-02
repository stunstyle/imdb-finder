package com.stunstyle.imdb.finder.command.file;

import com.stunstyle.imdb.finder.exception.MovieNotFoundException;
import com.stunstyle.imdb.finder.util.CommandParser;
import com.stunstyle.imdb.finder.util.FileManager;

public class GetPoster implements FileCommand {
    private String command;

    public GetPoster(String command) {
        this.command = command;
    }

    @Override
    public byte[] execute() {

        String movieName = CommandParser.getCommandParser().getMovieName(command);
        FileManager fm = FileManager.getFM();
        if (!fm.posterExistsInCache(movieName)) {
            try {
                fm.downloadPoster(movieName);
                return fm.getPosterBytes(movieName);
            } catch (MovieNotFoundException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                return new byte[0];
            }
        } else {
            return fm.getPosterBytes("Titanic");
        }

    }

}
