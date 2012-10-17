
package com.github.shimal.core.validators;

import com.github.shimal.core.constraints.HourRange;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.beanutils.PropertyUtils;



public class HourRangeValidator implements ConstraintValidator<HourRange, Object> {



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private String finishHour;
    private String startHour;



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    @Override
    public void initialize(HourRange constraintAnnotation) {

        startHour  = constraintAnnotation.startHour();
        finishHour = constraintAnnotation.endHour();
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean isValid(Object instance, ConstraintValidatorContext context) {

        boolean valid = false;

        try {
            Object startHourProp  = PropertyUtils.getProperty(instance, startHour);
            Object finishHourProp = PropertyUtils.getProperty(instance, finishHour);

            if (startHourProp != null && finishHourProp != null) {

                String startHourText  = startHourProp.toString();
                String finishHourText = finishHourProp.toString();

                if (startHourText.isEmpty() && finishHourText.isEmpty()) {
                    valid = true;
                } else {

                    long startHourNumber  = Long.parseLong(startHourText.replace(":", "").trim());
                    long finishHourNumber = Long.parseLong(finishHourText.replace(":", "").trim());

                    if (startHourNumber >= finishHourNumber) {
                        String violation = "Başlangıç saat aralığı bitiş saat aralığından büyük olamaz.";
                        addViolationForStartHour(context, violation);
                    } else {
                        valid = true;
                    }
                }
            }
        } catch (Exception ex) {
            //
        }

        return valid;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    private void addViolationForStartHour(ConstraintValidatorContext context, String message) {

        context.buildConstraintViolationWithTemplate(message).addNode(startHour).addConstraintViolation();
    }
}
