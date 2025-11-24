package com.codegym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CcdvSearchRequest {
    private String name;      // search text
    private Integer ageFrom;
    private Integer ageTo;
    private String gender;
    private String city;
    private String sort;      // view_desc, view_asc, hire_desc, hire_asc
    private Integer page = 0;
    private Integer size = 12;
}