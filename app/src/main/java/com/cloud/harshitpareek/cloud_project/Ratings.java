package com.cloud.harshitpareek.cloud_project;

/**
 * Created by harshitpareek on 5/11/17.
 */

public class Ratings
{
    private String level;
    private String text;

    public Ratings(String level, String text) {
        this.level = level;
        this.text = text;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
