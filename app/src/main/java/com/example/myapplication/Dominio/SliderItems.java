package com.example.myapplication.Dominio;

import com.example.myapplication.Clases.Movies;

public class SliderItems {
    private int image;
    private Movies movie;

    public SliderItems(int image, Movies movie) {
        this.image = image;
        this.movie = movie;
    }

    public SliderItems(Movies movie) {

    this.movie = movie;
    }


    public SliderItems(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
