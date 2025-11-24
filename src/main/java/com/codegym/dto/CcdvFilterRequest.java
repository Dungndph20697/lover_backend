package com.codegym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CcdvFilterRequest {
    private String name;
    private Integer minAge;
    private Integer maxAge;
    private String gender;
    private String address;
}
