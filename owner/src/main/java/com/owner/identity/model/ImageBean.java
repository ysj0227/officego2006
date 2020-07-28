package com.owner.identity.model;

public class ImageBean {
    private boolean isNetImage;
    private String path;
    private int id;

    public ImageBean(boolean isNetImage, int id, String path) {
        this.isNetImage = isNetImage;
        this.path = path;
        this.id = id;
    }

    public boolean isNetImage() {
        return isNetImage;
    }

    public void setNetImage(boolean netImage) {
        isNetImage = netImage;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
