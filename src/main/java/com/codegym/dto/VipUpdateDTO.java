package com.codegym.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VipUpdateDTO {
    private Long userId;
    private Boolean isVip;
}