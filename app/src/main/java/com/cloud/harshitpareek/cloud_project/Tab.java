package com.cloud.harshitpareek.cloud_project;

/**
 * Created by harshitpareek on 5/9/17.
 */

public class Tab
{
    private final int name;
    private final int imageResource;
    private boolean isFavorite = false;

    public Tab(int name, int imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    public int getName() {
        return name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }
    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public void toggleFavorite() {
        isFavorite = !isFavorite;
    }
}
