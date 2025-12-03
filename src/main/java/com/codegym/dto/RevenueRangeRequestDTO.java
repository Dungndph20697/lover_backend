package com.codegym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RevenueRangeRequestDTO {
    private String start;   // yyyy-MM-dd
    private String end;     // yyyy-MM-dd
}