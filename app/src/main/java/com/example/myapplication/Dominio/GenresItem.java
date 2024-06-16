package com.example.myapplication.Dominio;

public class GenresItem {
    private String name;
    private Integer id;

    public GenresItem(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    public GenresItem() {
    }

    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
