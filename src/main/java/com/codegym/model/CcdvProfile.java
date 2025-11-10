package com.codegym.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "ccdv_profiles")
public class CcdvProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String fullName;
    private Integer yearOfBirth;

    private String gender;

    private String city;
    private String nationality;
    private String avatar;
    private String portrait1;
    private String portrait2;
    private String portrait3;

    private Float height;
    private Float weight;

    private String hobbies;

    private String description;

    private String requirement;

    private String facebookLink;
    private LocalDateTime joinDate;
    private Integer hireCount;

}
