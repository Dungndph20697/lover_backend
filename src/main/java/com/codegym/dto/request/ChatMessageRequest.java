package com.codegym.dto.request;

import lombok.Data;

@Data
public class ChatMessageRequest {
    private Long senderId;
    private Long receiverId;
    private String content;
}
