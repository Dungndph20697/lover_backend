package com.codegym.dto;

import com.codegym.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopRecentCustomerDTO {
    private User user;
    private LocalDateTime lastHireTime;
}