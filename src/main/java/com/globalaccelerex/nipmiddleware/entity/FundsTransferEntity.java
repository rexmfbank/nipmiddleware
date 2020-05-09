package com.globalaccelerex.nipmiddleware.entity;

import com.globalaccelerex.nipmiddleware.enums.PaymentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

import static com.globalaccelerex.nipmiddleware.enums.PaymentStatusEnum.*;

@Data
@Table
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FundsTransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private BigDecimal amount;


    private String nameEnquiryReference;// client may not send it as first

    @NotBlank
    private String sessionId;

    @NotBlank
    private String clientId;


    private String channelCode;

    @NotBlank
    private String destinationInstitutionCode;

    private String beneficiaryAccountName;

    @NotBlank
    @Column(nullable = false)
    private String beneficiaryAccountNo;


    private String beneficiaryBVN;


    private String beneficiaryKYCLevel;

    @NotBlank
    private String originatorAccountName;

    @NotBlank
    private String originatorAccountNo;

    @NotBlank
    private String originatorInstitutionCode;

    @NotBlank
    private String originatorBVN;

    @NotBlank
    private String originatorKYCLevel;


    private String transactionLocation;

    @NotBlank
    private String narration;

    private String paymentReference;

    private String responseCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatusEnum paymentStatusEnum;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;


    @Transient
    public boolean isPending(){
        return paymentStatusEnum == PENDING;
    }

    @Transient
    public boolean isFtComplete(){
        return (paymentStatusEnum == FAILED) || (paymentStatusEnum == SUCCESS);
    }
}
