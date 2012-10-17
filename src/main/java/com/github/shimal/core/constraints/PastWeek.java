
package com.github.shimal.core.constraints;

import com.github.shimal.core.validators.PastWeekValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;



@Constraint(validatedBy = PastWeekValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE })
public @interface PastWeek {


    public String endYear();



    public String endWeek();



    public String message() default "Tarih şimdiki zamandan eski olmak zorunda!";



    public Class<?>[] groups() default {};



    public Class<? extends Payload>[] payload() default {};
}
