package com.xqk.nest.websocket.model;

public class SystemMessage {
    private boolean system;
    private long id;
    private String type;
    private String content;

    public SystemMessage() {
    }

    public SystemMessage(boolean system, long id, String type, String content) {
        this.system = system;
        this.id = id;
        this.type = type;
        this.content = content;
    }

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "SystemMessage{" +
                "system=" + system +
                ", id=" + id +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
