package com.codegym.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "hire_sessions_ccdv_service_details")
public class HireSessionCcdvservicedetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ccdv_service_detail_id", nullable = false)
    private CcdvServiceDetail ccdvServiceDetail;


    @ManyToOne
    @JoinColumn(name = "hireSession_id", nullable = false)
    private HireSession hireSession;

}
