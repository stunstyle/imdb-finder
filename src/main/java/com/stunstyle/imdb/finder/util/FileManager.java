package com.stunstyle.imdb.finder.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import com.stunstyle.imdb.finder.exception.MovieNotFoundException;
import com.stunstyle.imdb.finder.exception.SeriesOrSeasonNotFoundException;

public class FileManager {
    // responsible for file downloading and creation

    private static FileManager fm = new FileManager();

    private FileManager() {

    }

    public static FileManager getFM() {
        return fm;
    }

    public void downloadMovie(String movieName) throws MovieNotFoundException {
        URLBuilder builder = URLBuilder.getBuilder();
        URL url = builder.buildOmdbMovieQuery(movieName);
        HTTPConnector connector = HTTPConnector.getConnector();
        HttpURLConnection conn = connector.connect(url);

        if (!JSONManager.getJSONManager().isValidResponse(conn)) {
            throw new MovieNotFoundException("Movie not found!");
        }
        conn.disconnect();
        Path moviePath = createFileForMovie(movieName);

        conn = connector.connect(url);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
             PrintWriter pw = new PrintWriter(new FileWriter(moviePath.toString(), true))) {
            String output;
            while ((output = br.readLine()) != null) {
                pw.println(output);
            }
        } catch (IOException e) {
            System.err.println("Error while downloading movie file via HTTP!");
            e.printStackTrace();
        }
    }

    public void downloadSeriesSeason(String seriesName, int season) throws SeriesOrSeasonNotFoundException {
        URLBuilder builder = URLBuilder.getBuilder();
        URL url = builder.buildOmdbSeriesSeasonQuery(seriesName, season);

        HTTPConnector connector = HTTPConnector.getConnector();
        HttpURLConnection conn = connector.connect(url);

        if (!JSONManager.getJSONManager().isValidResponse(conn)) {
            throw new SeriesOrSeasonNotFoundException("Series or season not found!");
        }
        conn.disconnect();
        Path seriesSeasonPath = createFileForSeriesSeason(seriesName, season);
        conn = connector.connect(url);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
             PrintWriter pw = new PrintWriter(new FileWriter(seriesSeasonPath.toString(), true))) {
            String output;
            while ((output = br.readLine()) != null) {
                pw.println(output);
            }
        } catch (IOException e) {
            System.err.println("Error while reading file via HTTP!");
            e.printStackTrace();
        }
    }

    public void downloadPoster(String movieName) throws MovieNotFoundException {
        if (!movieExistsInCache(movieName)) {
            downloadMovie(movieName);
        }
        JSONManager jsonManager = JSONManager.getJSONManager();

        String imdbID = jsonManager.getImdbIdFromLocalJson(movieName);
        URLBuilder builder = URLBuilder.getBuilder();
        URL TMDBUrl = builder.buildTmdbFindQuery(imdbID);

        HTTPConnector connector = HTTPConnector.getConnector();
        HttpURLConnection conn = connector.connect(TMDBUrl);

        String poster_path = jsonManager.getPosterPathFromJsonViaHTTP(conn);
        URL TMDBConfigurationURL = builder.buildTmdbConfigurationQuery();

        conn = connector.connect(TMDBConfigurationURL);
        String baseURLString = jsonManager.getBaseUrlFromJsonViaHTTP(conn);

        URL finalURL = builder.buildFinalURLForPoster(baseURLString, poster_path);
        try (InputStream in = finalURL.openStream()) {
            Files.copy(in, getPathForPoster(movieName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error while downloading poster!");
            e.printStackTrace();
        }
    }

    public Path createFileForMovie(String movieName) {
        Path path = Paths.get("cache//" + movieName + ".json");
        try {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        } catch (IOException e) {
            System.err.println("Unable to create file!");
            e.printStackTrace();
        }
        return path;
    }

    public Path createFileForSeriesSeason(String seriesName, int season) {
        Path path = Paths.get("cache//" + seriesName + "_season_" + season + ".json");
        try {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        } catch (IOException e) {
            System.err.println("Unable to create file!");
            e.printStackTrace();
        }
        return path;
    }

    public Path getPathForPoster(String movieName) {
        return Paths.get("cache//posters//" + movieName + ".jpg");
    }

    public boolean movieExistsInCache(String movieName) {
        return new File("cache", movieName + ".json").exists();
    }

    public boolean tvSeriesSeasonExistsInCache(String seriesName, int season) {
        return new File("cache", seriesName + "_season_" + season + ".json").exists();
    }

    public boolean posterExistsInCache(String movieName) {
        return new File("cache//posters", movieName + ".jpg").exists();
    }

    public byte[] getPosterBytes(String movieName) {
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(Paths.get("cache//posters//" + movieName + ".jpg"));
        } catch (IOException e) {
            System.err.println("Error while reading image bytes!");
            e.printStackTrace();
        }
        return bytes;
    }

    public List<MovieRatingPair> getAllCachedMoviePairs() {
        List<Path> jsonFiles = getAllJsonFiles();
        List<MovieRatingPair> pairs = new ArrayList<>();
        JSONManager jsonManager = JSONManager.getJSONManager();
        for (Path file : jsonFiles) {
            try (BufferedReader br = new BufferedReader(new FileReader(file.toFile()))) {
                if (!file.getFileName().toString().contains("_season_")) {
                    pairs.add(new MovieRatingPair(jsonManager.getField(br, "Title"),
                            jsonManager.getField(br,"imdbRating")));
                }
            } catch (IOException e) {
                System.err.println("Error while reading files!");
                e.printStackTrace();
            }
        }
        return pairs;

    }

    public List<Path> getAllJsonFiles() {
        List<Path> jsonFiles = new ArrayList<>();
        try (DirectoryStream<Path> fileStream = Files.newDirectoryStream(Paths.get("cache//"), "*.json")) {
            fileStream.forEach(jsonFiles::add);
        } catch (IOException e) {
            System.err.println("Error while traversing files!");
            e.printStackTrace();
        }
        return jsonFiles;
    }
}
