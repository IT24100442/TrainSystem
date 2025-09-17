package org.example.trainsystem.dto;

public class MessageRequest {
    private String receiverId;
    private String messageText;

    // Constructors
    public MessageRequest() {}

    public MessageRequest(String receiverId, String messageText) {
        this.receiverId = receiverId;
        this.messageText = messageText;
    }

    // Getters and Setters
    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }

    public String getMessageText() { return messageText; }
    public void setMessageText(String messageText) { this.messageText = messageText; }
}
