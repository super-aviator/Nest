package com.xqk.nest.dto;

public class Tuple<T,E>{
    private T t;
    private E e;

    public Tuple(T t, E e) {
        this.t = t;
        this.e = e;
    }

    public Tuple() {
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public E getE() {
        return e;
    }

    public void setE(E e) {
        this.e = e;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "t=" + t +
                ", e=" + e +
                '}';
    }
}
