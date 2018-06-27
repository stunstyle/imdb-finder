package com.stunstyle.imdb.finder.util;

public class MovieRatingPair implements Comparable<MovieRatingPair> {
    private String movieName;
    private double movieRating;

    public MovieRatingPair(String movieName, String movieRating) {
        this.movieName = movieName;
        if (movieRating.equals("N/A")) {
            this.movieRating = -1;
        } else {
            this.movieRating = Double.parseDouble(movieRating);
        }
    }

    public String getMovieName() {
        return movieName;
    }

    public double getMovieRating() {
        return movieRating;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((movieName == null) ? 0 : movieName.hashCode());
        long temp;
        temp = Double.doubleToLongBits(movieRating);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MovieRatingPair other = (MovieRatingPair) obj;
        if (movieName == null) {
            if (other.movieName != null)
                return false;
        } else if (!movieName.equals(other.movieName))
            return false;
        if (Double.doubleToLongBits(movieRating) != Double.doubleToLongBits(other.movieRating))
            return false;
        return true;
    }

    @Override
    public int compareTo(MovieRatingPair o) {
        if (this.movieRating < o.movieRating)
            return -1;
        if (this.movieRating == o.movieRating) {
            return 0;
        }
        if (this.movieRating > o.movieRating) {
            return 1;
        }
        return 0;
    }

}
