package com.codegym.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChatUserResponse {
    private Long userId;
    private String name;
    private String nickname;
    private String lastMessage;
    private LocalDateTime lastTime;
}
