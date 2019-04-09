package com.xqk.nest.model;

public class UploadImageMod {
    private String src;

    public UploadImageMod() {
    }

    public UploadImageMod(String src) {
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
        return "UploadImageMod{" +
                "src='" + src + '\'' +
                '}';
    }
}
