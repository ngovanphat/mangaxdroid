package com.example.mangaxdroid.object;

import java.util.List;

public class Chapter {
    private String name;
    private String date;
    private String view;
    private int id;
    private List<String> imageURL;

    public Chapter(){}
    public Chapter(String name, String date, String view) {
        this.name = name;
        this.date = date;
        this.view = view;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public List<String> getPagesURL() {
        return imageURL;
    }

    public void setPagesURL(List<String> pagesURL) {
        this.imageURL = pagesURL;
    }
}
