package com.codegym.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CcdvServiceDTO {
    private Long serviceDetailId;
    private Integer serviceId;
    private String serviceName;
    private BigDecimal totalPrice;
    private Boolean isActive;
    private String note;
}
