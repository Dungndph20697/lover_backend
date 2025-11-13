package com.codegym.dto;

import com.codegym.model.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CcdvProfileDTO {
    private String fullName;
    private Integer yearOfBirth;
    private String gender;
    private String city;
    private String nationality;
    private Float height;
    private Float weight;
    private String hobbies;
    private String description;
    private String requirement;
    private String facebookLink;

    private MultipartFile avatar;
    private MultipartFile portrait1;
    private MultipartFile portrait2;
    private MultipartFile portrait3;

    private Long userId;
}