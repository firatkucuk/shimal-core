
package com.github.shimal.core.constraints;

import com.github.shimal.core.validators.HourRangeValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;



@Constraint(validatedBy = HourRangeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE })
public @interface HourRange {


    public String startHour();



    public String endHour();



    public String message() default "Girilen saat yanlış aralıkta!";



    public Class<?>[] groups() default {};



    public Class<? extends Payload>[] payload() default {};
}
