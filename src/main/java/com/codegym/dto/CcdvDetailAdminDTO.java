package com.codegym.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CcdvDetailAdminDTO {
    private Long userId;
    private String fullName;
    private String nickname;
    private String email;
    private String phone;
    private String cccd;
    private String city;
    private Integer yearOfBirth;

    // ẢNH
    private String avatar;
    private String portrait1;
    private String portrait2;
    private String portrait3;

    private Float height;
    private Float weight;
    private String description;
    private String requirement;
}
