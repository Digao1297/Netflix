package br.com.netflix.model;

public class Movie {
    private  int id;
    private String title;
    private String desc;
    private String cast;
    private String coverUrl;

    public Movie(int id, String coverUrl) {
        this.id = id;
        this.coverUrl = coverUrl;
    }

    public Movie(int id, String title, String desc, String cast, String coverUrl) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.cast = cast;
        this.coverUrl = coverUrl;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getCast() {
        return cast;
    }

    public String getCoverUrl() {
        return coverUrl;
    }
}
