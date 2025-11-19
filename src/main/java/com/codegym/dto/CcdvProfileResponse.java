package com.codegym.dto;

import lombok.Data;

@Data
public class CcdvProfileResponse {
    private Long id;
    private Long userId;

    private String fullName;
    private Integer yearOfBirth;
    private String gender;
    private String city;
    private String nationality;

    private String avatar;
    private String portrait1;
    private String portrait2;
    private String portrait3;

    private Float height;
    private Float weight;

    private String hobbies;
    private String description;
    private String requirement;
    private String facebookLink;

    private String joinDate;
    private Integer hireCount;
    private Long viewCount;
}
