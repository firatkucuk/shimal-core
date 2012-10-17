
package com.github.shimal.core.validators;

import com.github.shimal.core.constraints.PastWeek;
import java.util.Calendar;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.beanutils.PropertyUtils;



public class PastWeekValidator implements ConstraintValidator<PastWeek, Object> {



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private String endWeekFieldName;
    private String endYearFieldName;



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    @Override
    public void initialize(PastWeek constraintAnnotation) {

        endWeekFieldName = String.valueOf(constraintAnnotation.endWeek());
        endYearFieldName = String.valueOf(constraintAnnotation.endYear());
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean isValid(Object instance, ConstraintValidatorContext context) {

        try {
            int endWeek = (Byte) PropertyUtils.getProperty(instance, endWeekFieldName);
            int endYear = (Short) PropertyUtils.getProperty(instance, endYearFieldName);

            Calendar calender    = Calendar.getInstance();
            int      currentYear = new Integer(calender.get(Calendar.YEAR)).shortValue();
            int      currentWeek = calender.get(Calendar.WEEK_OF_YEAR);

            if (endYear > currentYear) {
                addViolationForYear(context, "Seçilen yıl güncel tarihten büyük olamaz");
            } else if (endYear == currentYear && endWeek > currentWeek) {
                addViolationForWeek(context, "Seçilen hafta güncel tarihten büyük olamaz");
            } else {
                return true;
            }
        } catch (Exception e) {
            // pass
        }

        return false;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    private void addViolationForWeek(ConstraintValidatorContext context, String message) {

        context.buildConstraintViolationWithTemplate(message).addNode(endWeekFieldName).addConstraintViolation();
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    private void addViolationForYear(ConstraintValidatorContext context, String message) {

        context.buildConstraintViolationWithTemplate(message).addNode(endYearFieldName).addConstraintViolation();
    }
}
