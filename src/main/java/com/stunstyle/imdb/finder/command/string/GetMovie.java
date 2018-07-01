package com.stunstyle.imdb.finder.command.string;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.stunstyle.imdb.finder.exception.MovieNotFoundException;
import com.stunstyle.imdb.finder.util.CommandParser;
import com.stunstyle.imdb.finder.util.FileManager;
import com.stunstyle.imdb.finder.util.JSONManager;

import java.io.*;

//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

public class GetMovie implements StringCommand {
    private String command;

    public GetMovie(String command) {
        this.command = command;
    }

    @Override
    public String execute() {
        // String toReturn = null;
        String movieName = CommandParser.getCommandParser().getMovieName(command);
        FileManager fm = FileManager.getFM();
        JSONManager jm = JSONManager.getJSONManager();

        if (!fm.movieExistsInCache(movieName)) {
            try {
                fm.downloadMovie(movieName);
            } catch (MovieNotFoundException e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                return e.getMessage();
            }
        }
        String[] fields = CommandParser.getCommandParser().getFields(command);

        if (fields == null) {
            File file = new File("cache", movieName + ".json");
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                return jm.getAllFields(br);

            } catch (FileNotFoundException e1) {
                System.err.println("File not found while getting movie!");
                e1.printStackTrace();
            } catch (IOException e1) {
                System.err.println("Error while reading and parsing JSON in GetMovie!");
                e1.printStackTrace();
            }
        } else

        {
            return jm.getJSONFields(movieName, fields);
        }
        return "Unknown";
    }

}
