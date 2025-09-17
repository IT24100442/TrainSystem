package org.example.trainsystem.dto;

public class MessageRequest {
    private int receiverId;
    private String messageText;

    // Constructors
    public MessageRequest() {}

    public MessageRequest(int receiverId, String messageText) {
        this.receiverId = receiverId;
        this.messageText = messageText;
    }

    // Getters and Setters
    public int getReceiverId() { return receiverId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }

    public String getMessageText() { return messageText; }
    public void setMessageText(String messageText) { this.messageText = messageText; }
}
