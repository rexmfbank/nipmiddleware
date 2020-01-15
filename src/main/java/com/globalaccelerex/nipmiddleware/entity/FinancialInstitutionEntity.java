package com.globalaccelerex.nipmiddleware.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Table
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancialInstitutionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name ="institution_code")
    private String institutionCode;

    @Column(name ="institution_name")
    private String institutionName;

    @Column(name ="category_code")
    private String categoryCode;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
}
