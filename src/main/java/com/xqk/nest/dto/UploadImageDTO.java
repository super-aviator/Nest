package com.xqk.nest.dto;

public class UploadImageDTO {
    private String src;

    public UploadImageDTO() {
    }

    public UploadImageDTO(String src) {
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
        return "UploadImageDTO{" +
                "src='" + src + '\'' +
                '}';
    }
}
