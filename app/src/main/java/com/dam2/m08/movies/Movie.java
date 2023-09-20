package com.dam2.m08.movies;

import java.io.Serializable;

public class Movie implements Serializable
{
    //region Variables
    private String title, img, vote, overview, originalTitle, originalLanguage;
    private int id, voteCount;
    private double popularity;
    //endregion

    //region Constructors
    public Movie(int id, String vote, String title, String img, String overview, String originalTitle, String originalLanguage, int voteCount, double popularity)
    {
        this.id = id;
        this.overview = overview;
        this.vote = vote;
        this.title = title;
        this.img = img;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.voteCount = voteCount;
        this.popularity = popularity;
    }
    public Movie(){}
    //endregion

    //region Getters i Setters
    public String getOriginalTitle() {return originalTitle;}
    public void setOriginalTitle(String originalTitle) {this.originalTitle = originalTitle;}

    public String getOriginalLanguage() {return originalLanguage;}
    public void setOriginalLanguage(String originalLanguage) {this.originalLanguage = originalLanguage;}

    public Double getPopularity() {return popularity;}
    public void setPopularity(Double popularity) {this.popularity = popularity;}

    public int getVoteCount() {return voteCount;}
    public void setVoteCount(int voteCount) {this.voteCount = voteCount;}

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getOverview() {return overview;}
    public void setOverview(String overview) {this.overview = overview;}

    public String getVote() {return vote;}
    public void setVote(String vote) {this.vote = vote;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getImg() {return img;}
    public void setImg(String img) {this.img = img;}
    //endregion
}
