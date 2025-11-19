package com.codegym.service;

import com.codegym.dto.request.ChatMessageRequest;
import com.codegym.dto.response.ChatMessageResponse;
import com.codegym.dto.response.ChatUserResponse;
import com.codegym.model.Message;

import java.util.List;

public interface IMessageService {
    ChatMessageResponse saveMessage(ChatMessageRequest request);

    List<ChatMessageResponse> getConversation(Long u1, Long u2);

    List<ChatUserResponse> getChatUsers(Long userId);
}
