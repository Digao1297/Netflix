package br.com.netflix.model;

public class Movie {

    private int coverUrl;

    public Movie() {
    }

    public Movie(int coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getCoverUrl() {
        return coverUrl;
    }
}
