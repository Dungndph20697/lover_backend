package com.codegym.dto;

import com.codegym.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopFrequentCustomerDTO {
    private User user;
    private Long hireCount;
}
