package com.codegym.dto;

import com.codegym.model.enums.ProfileStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopViewLoverDTO {
    private Long userId;
    private String fullName;
    private String avatar;
    private String description;
    private Integer viewCount;
    private ProfileStatus status;
}
