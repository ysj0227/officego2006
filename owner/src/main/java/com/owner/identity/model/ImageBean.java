package com.owner.identity.model;

public class ImageBean {
    private boolean isNetImage;
    private String path;

    public ImageBean(boolean isNetImage, String path) {
        this.isNetImage = isNetImage;
        this.path = path;
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
}
