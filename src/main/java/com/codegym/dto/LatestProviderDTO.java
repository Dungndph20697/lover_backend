package com.codegym.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LatestProviderDTO {
    private Long id;
    private String fullName;
    private String city;
    private String avatar;
    private Integer yearOfBirth;
    private String gender;
}
