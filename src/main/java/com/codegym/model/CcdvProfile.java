package com.codegym.model;

import com.codegym.model.enums.ProfileStatus;
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
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;


    @Column(name = "year_of_birth")
    private Integer yearOfBirth;

    @Column(length = 20)
    private String gender;

    @Column(length = 100)
    private String city;


    @Column(length = 100)
    private String nationality;


    @Column(length = 255)
    private String avatar;


    @Column(length = 255)
    private String portrait1;


    @Column(length = 255)
    private String portrait2;


    @Column(length = 255)
    private String portrait3;


    private Float height;



    private Float weight;

    @Column(length = 255)
    private String hobbies;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requirement;

    @Column(name = "facebook_link", length = 255)
    private String facebookLink;


    @Column(name = "join_date", nullable = false)
    private LocalDateTime joinDate;


    @Column(name = "hire_count")
    private Integer hireCount;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private ProfileStatus status = ProfileStatus.ACTIVE; // mặc định là ACTIVE

    // --- TRƯỜNG MỚI: setvip cho người ccdv
    @Column(name = "vip", nullable = false)
    private Boolean vip = false;

    @Column(name = "vip_start_time")
    private LocalDateTime vipStartTime;
}
