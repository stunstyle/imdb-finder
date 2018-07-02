package com.stunstyle.imdb.finder.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
                int count = fields.length;
                for (String s : fields) {
                    sb.append(s);
                    sb.append(":");
                    sb.append(json.get(s));
                    if (count-- != 0) {
                        sb.append(System.lineSeparator());
                    }
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
            JsonParser parser = new JsonParser();
            JsonElement jsonTree = parser.parse(br);
            if (jsonTree.isJsonObject()) {
                JsonObject json = jsonTree.getAsJsonObject();
                JsonArray movie_results = json.getAsJsonArray("movie_results");
                JsonElement my_result = movie_results.get(0);
                if (my_result.isJsonObject()) {
                    JsonObject my_result_object = my_result.getAsJsonObject();
                    poster_path = my_result_object.get("poster_path").toString().replaceAll("\"", "");
                }

            }
        } catch (IOException e) {
            System.err.println("IOException while reading json via HTTP!");
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return poster_path;
    }

    public String getBaseUrlFromJsonViaHTTP(HttpURLConnection conn) {
        String baseURL = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            JsonParser parser = new JsonParser();
            JsonElement jsonTree = parser.parse(br);
            if (jsonTree.isJsonObject()) {
                JsonObject json = jsonTree.getAsJsonObject();
                JsonElement images_jsonTree = json.get("images");
                if (images_jsonTree.isJsonObject()) {
                    JsonObject images_json = images_jsonTree.getAsJsonObject();
                    baseURL = images_json.get("base_url").toString().replaceAll("\"", "");
                }
            }

        } catch (IOException e) {
            System.err.println("IOException while reading json via HTTP!");
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
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

    public String[] getMovieTokens(String movieName, MovieToken token) {
        File file = new File("cache", movieName.replaceAll(" ", "%20") + ".json");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            JsonParser parser = new JsonParser();
            JsonElement jsonTree = parser.parse(br);
            if (jsonTree.isJsonObject()) {
                JsonObject json = jsonTree.getAsJsonObject();
                String tokens = json.get(token.getJsonId()).toString().replaceAll("\"", "").replaceAll(", ", ",");
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
        if (movieTokens == null) {
            return false;
        }
        return Arrays.asList(movieTokens).containsAll(Arrays.asList(tokens));
    }

    public boolean isValidResponse(HttpURLConnection conn) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            JsonParser parser = new JsonParser();
            JsonElement jsonTree = parser.parse(br);
            if (jsonTree.isJsonObject()) {
                JsonObject json = jsonTree.getAsJsonObject();
                if (json.get("Response").toString().replaceAll("\"", "").equals("True")) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error while reading JSON!");
            e.printStackTrace();
        }
        return false;
    }


    public MovieRatingPair constructPairFromReader(Reader reader) {
        JsonParser parser = new JsonParser();
        JsonElement jsonTree = parser.parse(reader);
        if (jsonTree.isJsonObject()) {
            JsonObject json = jsonTree.getAsJsonObject();
            return new MovieRatingPair(json.get("Title").toString().replaceAll("\"", ""), json.get("imdbRating").toString().replaceAll("\"", ""));
            // System.out.println(json.get("Title") + json.get("imdbRating").toString());
        }
        return null;
    }

    public String getAllFields(Reader reader) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(reader);
        return element.toString();
    }
}
