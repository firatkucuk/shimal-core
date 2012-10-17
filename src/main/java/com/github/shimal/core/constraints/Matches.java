
package com.github.shimal.core.constraints;

import com.github.shimal.core.validators.MatchesValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;



@Constraint(validatedBy = MatchesValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE })
public @interface Matches {


    public String message() default "Alanlar uyuşmuyor!";



    public Class<?>[] groups() default {};



    public Class<? extends Payload>[] payload() default {};



    public String[] fields();



    public String[] verifyFields();
}
