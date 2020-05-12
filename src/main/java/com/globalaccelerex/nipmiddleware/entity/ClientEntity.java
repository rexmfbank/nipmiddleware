package com.globalaccelerex.nipmiddleware.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name="client_entity")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique  =true , nullable = false , name = "client_id")
    private String clientId;

    @Column(unique  =true, nullable = false , name = "client_name")
    private String clientName;

    @Column(nullable = false)
    private String password;

    private boolean active ;

    @Column(name ="business_Desc")
    private String businessDesc;

    @Column(name ="callback_url")
    private String callbackUrl;

    @Column(name ="contact_email")
    private String contactEmail;

    @Column(name ="contact_phone")
    private String contactPhone;

    @Column(nullable = false)
    private String accountName;

    @Column(nullable = false)
    private String bvn;

    @Column(nullable = false)
    private String kycLevel;

    @Column(nullable = false)
    private String accountNo;

    private String bankCode;

    private String latitude ;

    private String longitude;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name ="created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name ="last_updated_at")
    private Date lastUpdatedAt;
}
