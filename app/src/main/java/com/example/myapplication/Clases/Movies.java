package com.example.myapplication.Clases;

import com.example.myapplication.Clases.Genres;

//{"id":44227,"title":"Inside-Out","release_date":"1999-01-01","poster_path":"/hmeL9wiGNSLKORfibkMOemmntkQ.jpg","runtime":7,"genres":[{"id":10749,"name":"Romance"},{"id":35,"name":"Comedy"}]}
//{
//    "id": 573435,
//    "title": "Bad Boys: Ride or Die",
//    "poster_path": "/nP6RliHjxsz4irTKsxe8FRhKZYl.jpg"
//  },

public class Movies {
    private int id;
    private String title;
    private String release_date;
    private String poster_path;
    private int runtime;
    private Genres[] genres;

    public Movies(int id, String title, String release_date, String poster_path, int runtime, Genres[] genres) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.runtime = runtime;
        this.genres = genres;
    }

    public Movies(int id, String title, String poster_path) {
        this.id = id;
        this.title = title;
        this.poster_path = poster_path;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public int getRuntime() {
        return runtime;
    }

    public Genres[] getGenres() {
        return genres;
    }
}
