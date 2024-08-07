
package com.example.myapplication.Dominio;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieItem {

    private static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/";

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("runtime")
    @Expose
    private Integer runtime;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("genres")
    @Expose
    private List<Genre> genres;
    @SerializedName("country")
    @Expose
    private List<String> country;
    @SerializedName("score")
    @Expose
    private Integer score;
    @SerializedName("favorite")
    @Expose
    private Boolean favorite;
    @SerializedName("actors")
    @Expose
    private List<Actor> actors;
    @SerializedName("trailer_path")
    @Expose
    private String trailerPath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<String> getCountry() {
        return country;
    }

    public void setCountry(List<String> country) {
        this.country = country;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean isFav() {
        return favorite;
    }

    public void setFav(Boolean favorite) {
        this.favorite = favorite;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public String getTrailerPath() {
        return trailerPath;
    }

    public void setTrailerPath(String trailerPath) {
        this.trailerPath = trailerPath;
    }

}
