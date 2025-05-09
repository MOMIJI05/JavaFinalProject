package com.example.service;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private String title;
    private String posterPath;
    private String ID;
    private String link;
    private String overview;
    private ArrayList<String> genres;
    private Boolean star;

    public Movie(String title, String posterPath, String ID, String link, String overview, ArrayList<String> genres, Boolean star){
        this.title = title;
        this.posterPath = posterPath;
        this.ID = ID;
        this.link = link;
        this.overview = overview;
        this.genres = genres;
        this.star = star;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getID() {
        return ID;
    }

    public String getLink() {
        return link;
    }

    public String getOverview() {
        return overview;
    }

    public List<String> getGenres() {
        return genres;
    }

    public Boolean getStar() {
        return star;
    }
}
