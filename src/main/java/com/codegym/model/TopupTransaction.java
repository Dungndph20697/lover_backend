package com.codegym.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "topup_transaction")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopupTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Double amount;

    private String bankTxnId;

    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();
}
