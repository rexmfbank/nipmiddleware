package com.globalaccelerex.nipmiddleware.mapper;


import com.globalaccelerex.nipmiddleware.entity.FinancialInstitutionEntity;
import com.globalaccelerex.nipmiddleware.payload.nip.inward.financialinstitution.Record;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Slf4j
@Service
public class NIPInwardMapper {

    public Function<Record, FinancialInstitutionEntity> mapFIEntity = record -> {
        return new FinancialInstitutionEntity().builder()
                .categoryCode(record.getCategory())
                .institutionCode(record.getInstitutionCode())
                .institutionName(record.getInstitutionName())
                .build();
    };
}
