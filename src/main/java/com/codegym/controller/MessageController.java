package com.codegym.controller;

import com.codegym.model.Message;
import com.codegym.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @PostMapping
    public Message sendMessage(@RequestBody Message message) {
        return messageService.sendMessage(message);
    }

    @GetMapping("/{orderId}")
    public List<Message> getMessages(@PathVariable("orderId") Long orderId) {
        return messageService.getMessagesByOrder(orderId);
    }
}
