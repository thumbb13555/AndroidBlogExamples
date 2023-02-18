package com.noahliu.horizontalrecyclerviewwithindicatorexample;

public class TanksData {

    private String title;
    private int image;
    private String depiction;

    public TanksData(String title, int image, String depiction) {
        this.title = title;
        this.image = image;
        this.depiction = depiction;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDepiction() {
        return depiction;
    }

    public void setDepiction(String depiction) {
        this.depiction = depiction;
    }
}
