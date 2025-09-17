package org.example.trainsystem.entity;

public class Train {
    private String tid;
    private String name;


    public Train() {
    }

    public Train(String tid, String name) {
        this.tid = tid;
        this.name = name;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
