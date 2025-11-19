package com.codegym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CcdvProfileHomeDTO {
    private Long id;
    private String fullName;
    private String avatar;
    private String description;
    private String gender;
    private List<ServiceTypeDTO> services;
    private Integer hireCount;
}