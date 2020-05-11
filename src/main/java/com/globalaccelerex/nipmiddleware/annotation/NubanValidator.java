package com.globalaccelerex.nipmiddleware.annotation;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NubanValidator implements ConstraintValidator<Nuban, String> {

    private boolean  ignoreEmpty ;

    @Override
    public void initialize(Nuban constraintAnnotation) {
        ignoreEmpty = constraintAnnotation.ignoreEmpty();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(ignoreEmpty && StringUtils.isBlank(value)){
            return true;
        }
        if(StringUtils.isBlank(value)) return false;
        return ( NumberUtils.isDigits(value) && value.length() ==  10);
    }
}
