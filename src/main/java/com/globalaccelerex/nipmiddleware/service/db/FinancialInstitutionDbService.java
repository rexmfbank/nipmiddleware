package com.globalaccelerex.nipmiddleware.service.db;

import com.globalaccelerex.nipmiddleware.entity.FinancialInstitutionEntity;
import com.globalaccelerex.nipmiddleware.repository.FinancialInstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jdo.annotations.Transactional;
import java.util.List;

@Service
public class FinancialInstitutionDbService {

    private final FinancialInstitutionRepository financialInstitutionRepository;

    @Autowired
    public FinancialInstitutionDbService(FinancialInstitutionRepository financialInstitutionRepository) {
        this.financialInstitutionRepository = financialInstitutionRepository;
    }

    public List<FinancialInstitutionEntity> findAll(){
        return financialInstitutionRepository.findByOrderByInstitutionNameAsc();
    }

    public void deleteRecords(){
        financialInstitutionRepository.deleteAll();
    }

    public void saveAll(List<FinancialInstitutionEntity> financialInstitutionEntityList){
        financialInstitutionRepository.saveAll(financialInstitutionEntityList);
    }

    @Transactional
    public void updateFIList(List<FinancialInstitutionEntity> financialInstitutionEntityList){
        deleteRecords();
        saveAll(financialInstitutionEntityList);
    }
}
