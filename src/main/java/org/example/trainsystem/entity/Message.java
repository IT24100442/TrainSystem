package org.example.trainsystem.entity;
import java.time.LocalDateTime;

public class Message {

    private Integer msgId;
    private int senderId;
    private int receiverId;
    private String msgText;
    private LocalDateTime msgTime;

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    // Transient field for display purposes
    private String driverName;

    // Constructors
    public Message() {}

    public Message(int senderId, int receiverId, String msgText, LocalDateTime msgTime) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.msgText = msgText;
        this.msgTime = msgTime;
    }

    // Getters and Setters
    public Integer getMsgId() { return msgId; }
    public void setMsgId(Integer msgId) { this.msgId = msgId; }

    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }

    public int getReceiverId() { return receiverId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }

    public String getMsgText() { return msgText; }
    public void setMsgText(String msgText) { this.msgText = msgText; }

    public LocalDateTime getMsgTime() { return msgTime; }
    public void setMsgTime(LocalDateTime msgTime) { this.msgTime = msgTime; }

}
