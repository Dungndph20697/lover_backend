package com.codegym.dto;

import com.codegym.model.CcdvServiceDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CcdvListItemServiceDTO {
    private Long id;
    private String fullName;
    private String avatar;
    private String description;
    private Boolean vip;
    private LocalDateTime joinDate;
    private BigDecimal minPricePerHour; // giá tiền/h (min)
    private List<ServicePreview> services; // up to 3 random

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServicePreview {
        private Long serviceId;
        private String serviceName;
        private BigDecimal pricePerHour;
    }
}
