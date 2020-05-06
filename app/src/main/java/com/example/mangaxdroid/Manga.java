package com.example.mangaxdroid;

import java.util.ArrayList;

public class Manga {
    private int id;
    private String author;
    private int viewCount;
    private boolean status;
    private String[] category;
    private String description;
    private ArrayList<Chapter> chapters;
    private int image;
    private String name;

    public Manga(int id,int image,String name,String author, int viewCount, boolean status, String[] category, String description, ArrayList<Chapter> chapters) {
        this.id=id;
        this.name = name;
        this.image=image;
        this.author = author;
        this.viewCount = viewCount;
        this.status = status;
        this.category = category;
        this.description = description;
        this.chapters = chapters;
    }

    public Manga(int image, String name) {
        this.image=image;
        this.name=name;
        this.author= this.description="";
        this.status=false;
        this.category=null;
        this.chapters=null;
        this.id=this.viewCount=0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String[] getCategory() {
        return category;
    }

    public void setCategory(String[] category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }
}
