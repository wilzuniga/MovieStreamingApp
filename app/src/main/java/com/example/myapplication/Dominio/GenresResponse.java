package com.example.myapplication.Dominio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GenresResponse {

    @SerializedName("genres")
    @Expose
    private List<GenresItem> genres = null;

    public List<GenresItem> getGenres() {
        return genres;
    }

    public void setGenres(List<GenresItem> genres) {
        this.genres = genres;
    }
}
