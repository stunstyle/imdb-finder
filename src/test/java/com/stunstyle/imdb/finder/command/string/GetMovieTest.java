package com.stunstyle.imdb.finder.command.string;

import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class GetMovieTest {

    @Test
    public void getMovieWithoutExplicitFieldsTest() {
        GetMovie getMovie = new GetMovie("get-movie Scarface");
        String actualResult = getMovie.execute();
        try {
            byte[] expectedResultBytes = Files.readAllBytes(Paths.get("cache//Scarface.json"));
            String expectedResult = new String(expectedResultBytes, StandardCharsets.UTF_8);
            assertTrue("return of execute() should equal the whole contents of Scarface.json",
                    actualResult.trim().equals(expectedResult.trim()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}