package com.codegym.repository;

import com.codegym.dto.response.ChatUserResponse;
import com.codegym.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
//    List<Message> findByOrderIdOrderByTimestampAsc(Long orderId);

    @Query("""
                SELECT m FROM Message m 
                WHERE 
                    (m.sender.id = :u1 AND m.receiver.id = :u2)
                 OR (m.sender.id = :u2 AND m.receiver.id = :u1)
                ORDER BY m.timestamp ASC
            """)
    List<Message> getConversation(Long u1, Long u2);

    @Query("""
                SELECT DISTINCT 
                    CASE WHEN m.sender.id = :userId THEN m.receiver.id 
                         ELSE m.sender.id 
                    END
                FROM Message m
                WHERE m.sender.id = :userId OR m.receiver.id = :userId
            """)
    List<Long> findChatUserIds(Long userId);

    @Query("""
                SELECT m FROM Message m
                WHERE (m.sender.id = :u1 AND m.receiver.id = :u2)
                   OR (m.sender.id = :u2 AND m.receiver.id = :u1)
                ORDER BY m.timestamp DESC
            """)
    List<Message> findLastMessage(Long u1, Long u2);

}