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
@Table(name = "funds_transfer_entity")
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

    @Column(name = "name_enquiry_reference")
    private String nameEnquiryReference;// client may not send it as first

    @NotBlank
    @Column(name = "session_id")
    private String sessionId;

    @NotBlank
    @Column(nullable = false,name = "client_id")
    private String clientId;

    @Column(name = "channel_code")
    private String channelCode;

    @NotBlank
    @Column(nullable = false,name = "destination_institution_code")
    private String destinationInstitutionCode;

    @Column(name = "beneficiary_account_name")
    private String beneficiaryAccountName;

    @NotBlank
    @Column(nullable = false,name = "beneficiary_account_no")
    private String beneficiaryAccountNo;

    @Column(name = "beneficiarybvn")
    private String beneficiaryBVN;

    @Column(name = "beneficiarykyclevel")
    private String beneficiaryKYCLevel;

    @Column(name = "originator_account_name")
    private String originatorAccountName;

    @NotBlank
    @Column(nullable = false , name = "originator_account_no")
    private String originatorAccountNo;

    @NotBlank
    @Column(nullable = false,name = "originator_institution_code")
    private String originatorInstitutionCode;

    private String originatorBVN;

    private String originatorKYCLevel;

    @Column(name = "transaction_location")
    private String transactionLocation;

    private String narration;

    @Column(name = "payment_reference")
    private String paymentReference;

    @Column(name = "response_code")
    private String responseCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatusEnum paymentStatusEnum;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated")
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
