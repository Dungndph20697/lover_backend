package com.codegym.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HireRequestDTO {

    // id người CCDV được thuê (User.id)
    private Long ccdvId;

    // Danh sách id ccdv_service_detail được chọn
    private List<Long> serviceDetailIds;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String address;
    private String message;
}
