package com.globalaccelerex.nipmiddleware.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AmountValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AmountConstraint {

    String message() default " Amount is not valid ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}