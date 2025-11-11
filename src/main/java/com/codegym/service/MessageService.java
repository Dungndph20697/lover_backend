package com.codegym.service;

import com.codegym.model.Message;
import com.codegym.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getMessagesByOrder(Long orderId) {
        return messageRepository.findByOrderIdOrderByTimestampAsc(orderId);
    }

    public Message sendMessage(Message message) {
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }
}