package com.xqk.nest.model;

public class UploadImageModel {
    private String src;

    public UploadImageModel() {
    }

    public UploadImageModel(String src) {
        this.src = src;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    @Override
    public String toString() {
        return "UploadImageModel{" +
                "src='" + src + '\'' +
                '}';
    }
}
