package com.codegym.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderLatestDTO {
    private Long id;
    private String fullName;
    private String avatar;
    private String description;
    private String requirement;
    private Double pricePerHour;
    private List<ServiceDTO> services; // 3 dịch vụ random
}

