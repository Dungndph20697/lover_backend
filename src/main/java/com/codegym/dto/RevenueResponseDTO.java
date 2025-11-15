package com.codegym.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RevenueResponseDTO {
    private Double revenue;      // tổng doanh thu
    private String currency;     // "VND" hoặc "USD"
    private String fromDate;     // yyyy-MM-dd
    private String toDate;       // yyyy-MM-dd
}