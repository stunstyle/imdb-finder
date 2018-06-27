package com.stunstyle.imdb.finder.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPConnector {
    // responsible for all HTTP connections
    private static final HTTPConnector connector = new HTTPConnector();

    private HTTPConnector() {

    }

    public static HTTPConnector getConnector() {
        return connector;
    }

    public HttpURLConnection connect(URL url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {

                throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
            }
        } catch (IOException e) {
            System.err.println("Unable to connect via HTTP!");
            e.printStackTrace();
        }
        return conn;
    }
}
