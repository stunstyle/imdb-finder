package com.stunstyle.imdb.finder.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONManager {
    // responsible for all work with JSON
    private static JSONManager jsonManager = new JSONManager();

    private JSONManager() {
    }

    public static JSONManager getJSONManager() {
        return jsonManager;
    }

    public String getJSONFields(String movieName, String[] fields) {
        String toReturn = null;
        File file = new File("cache", movieName + ".json");
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            JSONObject json = (JSONObject) (new JSONParser()).parse(br);
            for (String s : fields) {
                sb.append(s);
                sb.append(":");
                sb.append(json.get(s));
                sb.append(System.lineSeparator());
            }
            toReturn = sb.toString();
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IO Exception while reading JSON!");
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Error while parsing JSON!");
            e.printStackTrace();
        }
        return toReturn;
    }

    public String getEpisodesOfSeriesSeason(String seriesName, int season) {
        File file = new File("cache", seriesName + "_season_" + season + ".json");
        StringBuilder sb = new StringBuilder();
        String toReturn = null;
        JSONObject json;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            json = (JSONObject) (new JSONParser()).parse(br);
            JSONArray episodes = (JSONArray) json.get("Episodes");
            Iterator<?> iterator = episodes.iterator();
            sb.append("List of episodes of " + seriesName + ", season " + season);
            sb.append(System.lineSeparator());
            while (iterator.hasNext()) {
                JSONObject curr = ((JSONObject) iterator.next());
                sb.append(curr.get("Title"));
                sb.append(System.lineSeparator());
            }
            toReturn = sb.toString();
        } catch (FileNotFoundException e) {
            System.err.println("File not found while parsing JSON!");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Exception while reading JSON!");
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("ParseException in getEpisodesOfSeriesSeason!");
            e.printStackTrace();
        }
        return toReturn;

    }

    public String getPosterPathFromJsonViaHTTP(HttpURLConnection conn) {
        String poster_path = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            JSONObject json = (JSONObject) (new JSONParser()).parse(br);
            JSONArray movie_results = (JSONArray) json.get("movie_results");
            JSONObject my_result = (JSONObject) movie_results.get(0);
            poster_path = (String) my_result.get("poster_path");
        } catch (IOException e) {
            System.err.println("IOException while reading json via HTTP!");
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Exception while parsing json!");
            e.printStackTrace();
        }
        conn.disconnect();
        return poster_path;
    }

    public String getBaseUrlFromJsonViaHTTP(HttpURLConnection conn) {
        String baseURL = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            JSONObject json = (JSONObject) (new JSONParser()).parse(br);
            JSONObject images = (JSONObject) json.get("images");
            baseURL = (String) images.get("base_url");
        } catch (IOException e) {
            System.err.println("IOException while reading json via HTTP!");
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Exception while parsing json!");
            e.printStackTrace();
        }
        conn.disconnect();
        return baseURL;
    }

    public String getImdbIdFromLocalJson(String movieName) {
        String imdbID = null;
        File file = new File("cache", movieName + ".json");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            JSONObject json = (JSONObject) (new JSONParser()).parse(br);
            imdbID = (String) json.get("imdbID");

        } catch (FileNotFoundException e) {
            System.err.println("File not found in getPoster!");
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Exception while parsing JSON!");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Exception while reading JSON!");
            e.printStackTrace();
        }
        return imdbID;
    }

    public String[] getMovieActors(String movieName) {
        File file = new File("cache", movieName + ".json");
        String actors = null;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            JSONObject json = (JSONObject) (new JSONParser()).parse(br);
            actors = (String) json.get("Actors");
        } catch (FileNotFoundException e) {
            System.err.println("File not found while getting actors!");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("I/O error while getting actors!");
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Error while parsing JSON while getting actors!");
            e.printStackTrace();
        }
        String[] actorsStrings = null;
        if (actors != null) {
            actorsStrings = actors.split(",");
        }
        for (String s : actorsStrings) {
            s = s.trim();
            // TODO: WITH STREAM
        }
        return actorsStrings;
    }

    public String[] getMovieGenres(String movieName) {
        File file = new File("cache", movieName + ".json");
        String genres = null;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            JSONObject json = (JSONObject) (new JSONParser()).parse(br);
            genres = (String) json.get("Genre");
        } catch (FileNotFoundException e) {
            System.err.println("File not found while trying to read json!");
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Exception while trying to parse json!");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("I/O error while trying to read json!");
            e.printStackTrace();
        }
        String[] genresStrings = null;
        if (genres != null) {
            genresStrings = genres.split(",");
        }
        for (String s : genresStrings) {
            s = s.trim();
            // TODO: WITH STREAM
        }
        return genresStrings;
    }

    public boolean movieHasGenres(String movieName, String[] genres) {

        String[] movieGenres = getMovieGenres(movieName);

        return Arrays.asList(movieGenres).containsAll(Arrays.asList(genres));
    }

    public boolean movieHasActors(String movieName, String[] actors) {

        String[] movieActors = getMovieActors(movieName);
        return Arrays.asList(movieActors).containsAll(Arrays.asList(actors));
    }

    public boolean isValidResponse(HttpURLConnection conn) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            JSONObject json = (JSONObject) (new JSONParser()).parse(br);
            if (((String) json.get("Response")).equals("False")) {
                return false;
            }
        } catch (ParseException e) {
            System.err.println("Error while parsing JSON!");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error while reading JSON!");
            e.printStackTrace();
        }
        return true;
    }

}
