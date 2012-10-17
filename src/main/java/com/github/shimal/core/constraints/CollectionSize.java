
package com.github.shimal.core.constraints;

import com.github.shimal.core.validators.CollectionSizeValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;



@Constraint(validatedBy = CollectionSizeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE, ElementType.FIELD })
public @interface CollectionSize {


    public int maxSize();



    public int minSize();



    public String message() default "Girilen eleman sayısı istenilenden farklı!";



    public Class<?>[] groups() default {};



    public Class<? extends Payload>[] payload() default {};
}
