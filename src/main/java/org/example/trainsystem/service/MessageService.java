package org.example.trainsystem.service;


import org.example.trainsystem.entity.Message;
import org.example.trainsystem.repository.MessageDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageDAO messageDAO;

    public void sendMessage(int senderId, int driverId, String text) {
        Message msg = new Message();
        msg.setSenderId(senderId);
        msg.setReceiverId(driverId);
        msg.setMsgText(text);
        messageDAO.save(msg); // inserts into DB
    }

    public List<Message> getMessagesForDriver(int driverId) {
        return messageDAO.findRecentMessagesByReceiverId(driverId,20);
    }
    public List<Message> getMessagesSentforManager(int managerId) {
        return messageDAO.findRecentMessagesByReceiverId(managerId,20);
    }
}
