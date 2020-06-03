package com.example.mangaxdroid.object;

public class Comment {
    private String content;
    private String date;



    private String userID;
    public Comment(){}
    public Comment(String content, String date,String userID) {
        this.content = content;
        this.date = date;
        this.userID = userID;
    }
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
