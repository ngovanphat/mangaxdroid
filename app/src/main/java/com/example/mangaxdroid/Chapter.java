package com.example.mangaxdroid;

import java.util.ArrayList;

public class Chapter {
    private String name;
    private String id;//Chapter "15.5","Bonus","1"
    private ArrayList<String> pagesURL;

    public ArrayList<String> getPagesURL() {
        return pagesURL;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setPagesURL(ArrayList<String> pagesURL) {
        this.pagesURL = pagesURL;
    }
}

