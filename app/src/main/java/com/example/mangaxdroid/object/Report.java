package com.example.mangaxdroid.object;

import java.util.Date;

public class Report {
    private String id;
    private String phoneModel;
    private String Topic;
    private String Chapter;
    private Manga mangaInfo;
    private String Manga;
    private String Details;
    private long createdAt;
    public Report(){};
    public Report(String id,String topic,String chapterId,String mangaId,String details,long createdAt){
        this.id=id;
        this.Topic=topic;
        this.Chapter=chapterId;
        this.Manga=mangaId;
        this.Details=details;
        this.createdAt=createdAt;
        this.phoneModel="";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }



    public String getTopic() {
        return Topic;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }

    public String getChapter() {
        return Chapter;
    }

    public void setChapter(String chapter) {
        Chapter = chapter;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getManga() {
        return Manga;
    }

    public void setManga(String manga) {
        Manga = manga;
    }

    public com.example.mangaxdroid.object.Manga getMangaInfo() {
        return mangaInfo;
    }

    public void setMangaInfo(com.example.mangaxdroid.object.Manga mangaInfo) {
        this.mangaInfo = mangaInfo;
    }
}
