
package com.github.shimal.core.validators;

import com.github.shimal.core.constraints.CollectionSize;
import java.util.Collection;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;



public class CollectionSizeValidator implements ConstraintValidator<CollectionSize, Collection> {



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private int maxSize;
    private int minSize;



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    @Override
    public void initialize(CollectionSize constraintAnnotation) {

        maxSize = constraintAnnotation.maxSize();
        minSize = constraintAnnotation.minSize();
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean isValid(Collection collection, ConstraintValidatorContext context) {

        boolean valid = true;

        try {

            if (collection.size() > maxSize) {
                valid = false;

                String message = "Belirtilen azami sayıdan daha fazla veri girdiniz!";

                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            } else if (collection.size() < minSize) {
                valid = false;

                String message = "Belirtilen asgari sayıdan daha az veri girdiniz!";

                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            }
        } catch (Exception ex) {
            valid = false;
        }

        return valid;
    }

}
