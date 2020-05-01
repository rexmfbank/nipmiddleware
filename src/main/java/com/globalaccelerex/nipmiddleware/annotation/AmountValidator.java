package com.globalaccelerex.nipmiddleware.annotation;

import lombok.val;
import org.apache.commons.lang3.math.NumberUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AmountValidator implements ConstraintValidator<AmountConstraint, String> {

    @Override
    public void initialize(AmountConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean response = true;
        try{
            val aDouble = NumberUtils.createDouble(value);
            if(aDouble <= 0 ){
                response = false;
            }
        }catch (NumberFormatException nfe){
            response = false;
        }
        return response;

    }
}

