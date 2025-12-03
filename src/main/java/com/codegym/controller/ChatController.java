package com.codegym.controller;

import com.codegym.dto.request.ChatMessageRequest;
import com.codegym.dto.response.ChatMessageResponse;
import com.codegym.service.IMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    @Autowired
    private IMessageService messageService;

    private final  SimpMessagingTemplate template;

    @MessageMapping("/chat/send")
    public void handleChatMessage(ChatMessageRequest request) {

        ChatMessageResponse response = messageService.saveMessage(request);

        // real time
        template.convertAndSend("/topic/chat/" + request.getSenderId(), response);
        template.convertAndSend("/topic/chat/" + request.getReceiverId(), response);
    }
}
