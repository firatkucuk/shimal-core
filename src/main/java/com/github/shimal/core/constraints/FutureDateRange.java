
package com.github.shimal.core.constraints;

import com.github.shimal.core.validators.FutureDateRangeValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;



@Constraint(validatedBy = FutureDateRangeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE })
public @interface FutureDateRange {


    public String startDate();



    public String endDate();



    public String message() default "Girilen tarih yanlış aralıkta!";



    public Class<?>[] groups() default {};



    public Class<? extends Payload>[] payload() default {};
}
