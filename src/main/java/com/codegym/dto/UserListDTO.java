package com.codegym.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserListDTO {
    private Long id;
    private String fullName;
    private String nickname;
    private String roleName;
    private String status;
}

