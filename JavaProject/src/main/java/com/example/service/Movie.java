package com.example.service;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private String title;
    private String posterPath;
    private int ID;
    private String link;
    private String overview;
    private ArrayList<String> genres;

    public Movie(String title, String posterPath, int ID, String link, String overview, ArrayList<String> genres){
        this.title = title;
        this.posterPath = posterPath;
        this.ID = ID;
        this.link = link;
        this.overview = overview;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public int getID() {
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

    public void setDetail(String title, String posterPath, int ID, String link, String overview, ArrayList<String> genres){
        this.title = title;
        this.posterPath = posterPath;
        this.ID = ID;
        this.link = link;
        this.overview = overview;
        this.genres = genres;
    }
}
