package br.com.netflix.model;

public class Movie {
    private String coverUrl;

    public Movie() {
    }

    public Movie( String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }
}
