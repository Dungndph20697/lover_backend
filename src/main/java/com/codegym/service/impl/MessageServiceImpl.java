package com.codegym.service.impl;

import com.codegym.dto.request.ChatMessageRequest;
import com.codegym.dto.response.ChatMessageResponse;
import com.codegym.dto.response.ChatUserResponse;
import com.codegym.model.Message;
import com.codegym.model.User;
import com.codegym.repository.MessageRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.IMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements IMessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    private ChatMessageResponse mapToDTO(Message msg) {
        return ChatMessageResponse.builder()
                .id(msg.getId())
                .senderId(msg.getSender().getId())
                .receiverId(msg.getReceiver().getId())
                .content(msg.getContent())
                .timestamp(msg.getTimestamp())
                .build();
    }

    @Override
    public ChatMessageResponse saveMessage(ChatMessageRequest request) {

        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(request.getContent())
                .timestamp(LocalDateTime.now())
                .build();

        return mapToDTO(messageRepository.save(message));
    }

    @Override
    public List<ChatMessageResponse> getConversation(Long u1, Long u2) {
        return messageRepository.getConversation(u1, u2)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatUserResponse> getChatUsers(Long userId) {
        List<Long> ids = messageRepository.findChatUserIds(userId);

        List<ChatUserResponse> result = new ArrayList<>();

        for (Long targetId : ids) {

            User user = userRepository.findById(targetId).orElse(null);
            if (user == null) continue;

            // tin nhắn mới nhất
            List<Message> last = messageRepository.findLastMessage(userId, targetId);
            Message lastMsg = last.isEmpty() ? null : last.get(0);

            result.add(new ChatUserResponse(
                    user.getId(),
                    user.getFirstName() + " " + user.getLastName(),
                    user.getNickname(),
                    lastMsg != null ? lastMsg.getContent() : "",
                    lastMsg != null ? lastMsg.getTimestamp() : null
            ));
        }

        // sắp xếp theo thời gian tin nhắn mới nhất
        result.sort((a, b) -> {
            if (a.getLastTime() == null) return 1;
            if (b.getLastTime() == null) return -1;
            return b.getLastTime().compareTo(a.getLastTime());
        });

        return result;
    }

}
