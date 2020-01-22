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
    @Column(nullable = false)
    private String sessionId;

    @NotBlank
    @Column(nullable = false)
    private String clientId;

    private String channelCode;

    @NotBlank
    @Column(nullable = false)
    private String destinationInstitutionCode;

    private String beneficiaryAccountName;

    @NotBlank
    @Column(nullable = false)
    private String beneficiaryAccountNo;

    private String beneficiaryBVN;

    private String beneficiaryKYCLevel;

    private String originatorAccountName;

    @NotBlank
    @Column(nullable = false)
    private String originatorAccountNo;

    @NotBlank
    @Column(nullable = false)
    private String originatorInstitutionCode;

    private String originatorBVN;

    private String originatorKYCLevel;

    private String transactionLocation;

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
