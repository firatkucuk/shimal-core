
package com.github.shimal.core.validators;

import com.github.shimal.core.constraints.FutureDateRange;
import java.util.Date;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.beanutils.PropertyUtils;



public class FutureDateRangeValidator implements ConstraintValidator<FutureDateRange, Object> {



    //~ --- [STATIC FIELDS/INITIALIZERS] -------------------------------------------------------------------------------

    private static final int AFTER  = 1;
    private static final int BEFORE = -1;
    private static final int EQUAL  = 0;



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private String endDateFieldName;
    private String startDateFieldName;



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    @Override
    public void initialize(FutureDateRange constraintAnnotation) {

        endDateFieldName   = String.valueOf(constraintAnnotation.endDate());
        startDateFieldName = String.valueOf(constraintAnnotation.startDate());
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean isValid(Object instance, ConstraintValidatorContext context) {

        boolean valid = true;

        try {
            Date   startDate = (Date) PropertyUtils.getProperty(instance, startDateFieldName);
            Date   endDate   = (Date) PropertyUtils.getProperty(instance, endDateFieldName);
            String message;


            if (startDate.compareTo(endDate) == AFTER) {
                message = "Başlangıç tarihi bitiş tarihinden büyük olamaz.";
                valid   = false;

                addViolationForStartDate(context, message);
            }
        } catch (Exception ex) {
            valid = false;
        }

        return valid;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    private void addViolationForEndDate(ConstraintValidatorContext context, String message) {

        context.buildConstraintViolationWithTemplate(message).addNode(endDateFieldName).addConstraintViolation();
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    private void addViolationForStartDate(ConstraintValidatorContext context, String message) {

        context.buildConstraintViolationWithTemplate(message).addNode(startDateFieldName).addConstraintViolation();
    }
}
