
package com.github.shimal.core.validators;

import com.github.shimal.core.constraints.MapSize;
import java.util.Map;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;



public class MapSizeValidator implements ConstraintValidator<MapSize, Map> {



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private int maxSize;
    private int minSize;



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    @Override
    public void initialize(MapSize constraintAnnotation) {

        maxSize = constraintAnnotation.maxSize();
        minSize = constraintAnnotation.minSize();
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean isValid(Map map, ConstraintValidatorContext context) {

        boolean valid = true;

        try {

            if (map.size() > maxSize) {
                valid = false;

                String message = "Belirtilen azami sayıdan daha fazla veri girdiniz!";

                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            } else if (map.size() < minSize) {
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
