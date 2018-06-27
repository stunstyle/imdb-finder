package com.stunstyle.imdb.finder.command.string;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClearCache implements StringCommand {
    public ClearCache() {
    }

    @Override
    public String execute() {
        try {
            Files.walk(Paths.get("cache/")).map(Path::toFile).forEach(File::delete);
            Files.createDirectories(Paths.get("cache/posters"));
        } catch (IOException e) {
            System.err.println("IOException while deleting files!");
            e.printStackTrace();
            return "Error!";
        }
        return "Cache cleared!";
    }

}
