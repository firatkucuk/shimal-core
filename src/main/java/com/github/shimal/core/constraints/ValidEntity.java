
package com.github.shimal.core.constraints;

import com.github.shimal.core.validators.ValidEntityValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;



@Constraint(validatedBy = ValidEntityValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface ValidEntity {


    public String message() default "Alan gerçek bir öğeye karşılık gelmiyor!";



    public Class<?>[] groups() default {};



    public Class<? extends Payload>[] payload() default {};



    public boolean nullable() default false;
}
