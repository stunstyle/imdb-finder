package com.stunstyle.imdb.finder.command.string;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

import com.stunstyle.imdb.finder.exception.MovieNotFoundException;
import com.stunstyle.imdb.finder.util.CommandParser;
import com.stunstyle.imdb.finder.util.FileManager;
import com.stunstyle.imdb.finder.util.JSONManager;

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
        String toReturn = JSONManager.getJSONManager().getJSONFields(movieName,fields);

        if (fields == null) {
            File file = new File("cache", movieName + ".json");
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(br);
                toReturn = element.toString();
                // toReturn = new Gson().fromJson(br, Object.class);// (new JSONParser()).parse(br).toString();
            } /*catch (ParseException e) {
                System.err.println("Error while parsing JSON!");
                e.printStackTrace();
            } */catch (FileNotFoundException e1) {
                System.err.println("File not found while getting movie!");
                e1.printStackTrace();
            } catch (IOException e1) {
                System.err.println("Error while reading and parsing JSON in GetMovie!");
                e1.printStackTrace();
            }
        } else {
            toReturn = JSONManager.getJSONManager().getJSONFields(movieName, fields);
        }
        return toReturn;
    }

}
