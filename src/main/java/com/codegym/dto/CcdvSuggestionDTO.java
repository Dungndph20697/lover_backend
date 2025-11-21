package com.codegym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CcdvSuggestionDTO {
    private Long profileId;
    private Long userId;
    private String name;
    private String avatar;
    private String description;
    private List<ServiceVipDTO> services; // up to 3 random
    private BigDecimal startingPricePerHour; // ví dụ: min price among services
}
