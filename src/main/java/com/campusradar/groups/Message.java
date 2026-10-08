package com.campusradar.groups;

import java.sql.Timestamp;

public class Message {
    private int id;
    private String userName;
    private String text;
    private Timestamp postedAt;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public Timestamp getPostedAt() { return postedAt; }
    public void setPostedAt(Timestamp postedAt) { this.postedAt = postedAt; }
}