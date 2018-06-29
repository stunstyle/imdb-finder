package com.stunstyle.imdb.finder.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.Arrays;

public class JSONManager {
    // responsible for all work with JSON
    private static JSONManager jsonManager = new JSONManager();

    private JSONManager() {
    }

    public static JSONManager getJSONManager() {
        return jsonManager;
    }

    public String getJSONFields(String movieName, String[] fields) {
        File file = new File("cache", movieName + ".json");
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            JsonParser parser = new JsonParser();
            JsonElement jsonTree = parser.parse(br);
            if (fields == null) {
                return jsonTree.toString();
            }
            if (jsonTree.isJsonObject()) {
                JsonObject json = jsonTree.getAsJsonObject();
                for (String s : fields) {
                    sb.append(s);
                    sb.append(":");
                    sb.append(json.get(s));
                    sb.append(System.lineSeparator());
                }
                return sb.toString();
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IO Exception while reading JSON!");
            e.printStackTrace();
        }
        return null;
    }

    public String getEpisodesOfSeriesSeason(String seriesName, int season) {
        File file = new File("cache", seriesName + "_season_" + season + ".json");
        StringBuilder sb = new StringBuilder();
        String toReturn = null;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            JsonParser parser = new JsonParser();
            JsonElement jsonTree = parser.parse(br);
            if (jsonTree.isJsonObject()) {
                JsonObject json = jsonTree.getAsJsonObject();
                sb.append("List of episodes of " + seriesName + ", season " + season);
                JsonArray episodes = json.getAsJsonArray("Episodes");
                for (JsonElement e : episodes) {
                    sb.append(e.getAsJsonObject().get("Title"));
                    sb.append(System.lineSeparator());
                }
                return sb.toString();
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found while parsing JSON!");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Exception while reading JSON!");
            e.printStackTrace();
        }
        return null;

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
        File file = new File("cache", movieName + ".json");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            JsonParser parser = new JsonParser();
            JsonElement jsonTree = parser.parse(br);
            return jsonTree.getAsJsonObject().get("imdbID").toString().replaceAll("\"", "");

        } catch (FileNotFoundException e) {
            System.err.println("File not found in getPoster!");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Exception while reading JSON!");
            e.printStackTrace();
        }
        return null;
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

    public String[] getMovieTokens(String movieName, MovieToken token) {
        File file = new File("cache", movieName + ".json");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            JsonParser parser = new JsonParser();
            JsonElement jsonTree = parser.parse(br);
            if(jsonTree.isJsonObject()) {
                JsonObject json = jsonTree.getAsJsonObject();
                String tokens = json.get(token.getJsonId()).toString().replaceAll("\"","").replaceAll(" ", "");
                return tokens.split(",");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean movieHasTokens(String movieName, String[] tokens, MovieToken token) {
        String[] movieTokens = getMovieTokens(movieName, token);
        return Arrays.asList(movieTokens).containsAll(Arrays.asList(tokens));
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
