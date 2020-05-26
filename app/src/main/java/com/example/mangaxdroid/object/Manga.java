package com.example.mangaxdroid.object;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class Manga implements Serializable {
    private String id;
    private String Name;
    private String Author;
    private int viewCount;
    private int status;
    private String category;
    private String description;
    private ArrayList<Chapter> chapters;
    private String image;

    public Manga(){}
    public Manga(String id, String image, String name, String author, int viewCount, int status, String category, String description) {
        this.id=id;
        this.Name = name;
        this.image=image;
        this.Author = author;
        this.viewCount = viewCount;
        this.status = status;
        this.category = category;
        this.description = description;
        this.chapters = null;
    }

    public Manga(String image, String name) {
        this.image=image;
        this.Name=name;
        this.Author= this.description="";
        this.status=0;
        this.category="";
        this.chapters=null;
        this.id="";
        this.viewCount=0;
    }

    public String getName() {
        return this.Name;
    }
    public String capitalizeFirstLetter(String text) {
        StringBuilder str = new StringBuilder();
        String[] tokens = text.split(" ");// Can be space,comma or hyphen
        Log.d("SizeString", String.valueOf(tokens.length));
        for (String token : tokens) {
            str.append(Character.toUpperCase(token.charAt(0))).append(token.substring(1)).append(" ");
        }
        str.toString().trim();
        return str.toString();
    }
    public void setName(String name) {
        this.Name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        this.Author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int isStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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

    public int getChaptersCount(){
        return chapters.size();
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }
}
