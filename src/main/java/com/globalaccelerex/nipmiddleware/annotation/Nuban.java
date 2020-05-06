package com.globalaccelerex.nipmiddleware.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NubanValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Nuban {

    String message() default " Account No is not a valid Nuban Account";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
