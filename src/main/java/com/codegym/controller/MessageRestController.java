package com.codegym.controller;

import com.codegym.dto.response.ChatMessageResponse;
import com.codegym.dto.response.ChatUserResponse;
import com.codegym.service.IMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageRestController {

    @Autowired
    private IMessageService messageService;

    @GetMapping("/chat-users/{u1}/{u2}")
    public List<ChatMessageResponse> getHistory(@PathVariable Long u1, @PathVariable Long u2) {
        return messageService.getConversation(u1, u2);
    }

    @GetMapping("/chat-users/{userId}")
    public List<ChatUserResponse> getChatUsers(@PathVariable Long userId) {
        return messageService.getChatUsers(userId);
    }
}
