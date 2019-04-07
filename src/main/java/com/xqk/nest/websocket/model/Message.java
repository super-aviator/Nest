package com.xqk.nest.websocket.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Message<T> {
    @JSONField(ordinal = 1)
    private String emit;

    @JSONField(ordinal = 2)
    private T data;

    public Message(T t,String emit){
        this.emit=emit;
        this.data =t;
    }

    public Message() {
    }

    public String getEmit() {
        return emit;
    }

    public void setEmit(String emit) {
        this.emit = emit;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "emit='" + emit + '\'' +
                ", data=" + data +
                '}';
    }
}
