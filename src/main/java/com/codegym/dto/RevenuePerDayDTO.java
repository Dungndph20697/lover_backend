package com.codegym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RevenuePerDayDTO {
    private LocalDate date;
    private Double revenue;
}
