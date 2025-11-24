package com.codegym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CcdvFindByCity {
    private Long id;
    private String fullName;
    private String avatar;
    private String description;
    private String city;
    private Integer hireCount;
}
