package org.example.trainsystem.entity;

public class Train {
    private int trainId;
    private String name;


    public Train() {
    }

    public Train(int tid, String name) {
        this.trainId = tid;
        this.name = name;
    }

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int tid) {
        this.trainId = tid;
    }

    public String getTrainName() {
        return name;
    }

    public void setTrainName(String name) {
        this.name = name;
    }
}
