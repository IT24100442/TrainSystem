package org.example.trainsystem.entity;
import java.time.LocalDateTime;

public class Message {

    private Integer msgId;
    private String senderId;
    private String receiverId;
    private String msgText;
    private LocalDateTime msgTime;

    // Constructors
    public Message() {}

    public Message(String senderId, String receiverId, String msgText, LocalDateTime msgTime) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.msgText = msgText;
        this.msgTime = msgTime;
    }

    // Getters and Setters
    public Integer getMsgId() { return msgId; }
    public void setMsgId(Integer msgId) { this.msgId = msgId; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }

    public String getMsgText() { return msgText; }
    public void setMsgText(String msgText) { this.msgText = msgText; }

    public LocalDateTime getMsgTime() { return msgTime; }
    public void setMsgTime(LocalDateTime msgTime) { this.msgTime = msgTime; }

}
