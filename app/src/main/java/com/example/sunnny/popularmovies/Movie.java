package com.example.sunnny.popularmovies;

/**
 * Created by Sunnny on 21/05/16.
 */
public class Movie {

    String id;
    String origional_title;
    String poster_url;
    String plot_synopsis;
    String user_rating;
    String release_date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrigional_title() {
        return origional_title;
    }

    public void setOrigional_title(String origional_title) {
        this.origional_title = origional_title;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public String getPlot_synopsis() {
        return plot_synopsis;
    }

    public void setPlot_synopsis(String plot_synopsis) {
        this.plot_synopsis = plot_synopsis;
    }

    public String getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(String user_rating) {
        this.user_rating = user_rating;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}
