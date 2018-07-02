package com.stunstyle.imdb.finder.util;

public enum MovieToken {

    ACTORS("Actors"),
    GENRE("Genre");


    private final String jsonId;

    MovieToken(String jsonId) {
        this.jsonId = jsonId;
    }

    public String getJsonId() {
        return jsonId;
    }
}
