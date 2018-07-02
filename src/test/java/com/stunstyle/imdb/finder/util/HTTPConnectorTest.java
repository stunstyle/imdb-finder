package com.stunstyle.imdb.finder.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.junit.Test;

import com.stunstyle.imdb.finder.util.HTTPConnector;
import com.stunstyle.imdb.finder.util.URLBuilder;

public class HTTPConnectorTest {

    @Test
    public void connectTest() {
        HTTPConnector connector = HTTPConnector.getConnector();

        HttpURLConnection conn = connector.connect(URLBuilder.getBuilder().buildOmdbMovieQuery("Titanic"));
        try {
            assertEquals(conn.getResponseCode(), HttpURLConnection.HTTP_OK);
        } catch (IOException e) {
            System.err.println("Error while connecting!");
            e.printStackTrace();
        }
    }

}
