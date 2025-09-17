package org.example.trainsystem.entity;

public class Train {
    private int tid;
    private String name;


    public Train() {
    }

    public Train(int tid, String name) {
        this.tid = tid;
        this.name = name;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
