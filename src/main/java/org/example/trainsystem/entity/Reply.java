package org.example.trainsystem.entity;

import java.time.LocalDateTime;

public class Reply {
    private int replyId;
    private int concernId;
    private int execId;
    private String replyText;
    private Concern concern;

    public Concern getConcern() {
        return concern;
    }

    public void setConcern(Concern concern) {
        this.concern = concern;
    }

    public int getExecId() {
        return execId;
    }

    public void setExecId(int execId) {
        this.execId = execId;
    }

    public LocalDateTime getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(LocalDateTime replyTime) {
        this.replyTime = replyTime;
    }

    private LocalDateTime replyTime;

    public Reply() {
    }

    public Reply(int replyId, int concernId, String replyText, LocalDateTime replyTime, Concern concern, int execId) {
        this.replyId = replyId;
        this.concernId = concernId;
        this.replyText = replyText;
        this.replyTime = replyTime;
        this.concern = concern;
        this.execId = execId;
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public int getConcernId() {
        return concernId;
    }

    public void setConcernId(int concernId) {
        this.concernId = concernId;
    }

    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }
}
