
package com.github.shimal.core.validators;

import com.github.shimal.core.constraints.Matches;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.beanutils.BeanUtils;



public class MatchesValidator implements ConstraintValidator<Matches, Object> {



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private String[] fields;
    private String[] verifyFields;



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    @Override
    public void initialize(Matches constraintAnnotation) {

        fields       = constraintAnnotation.fields();
        verifyFields = constraintAnnotation.verifyFields();
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean isValid(Object instance, ConstraintValidatorContext context) {

        boolean matches = true;

        for (int i = 0; i < fields.length; i++) {

            Object fieldObj, verifyFieldObj;

            try {
                fieldObj       = BeanUtils.getProperty(instance, fields[i]);
                verifyFieldObj = BeanUtils.getProperty(instance, verifyFields[i]);
            } catch (Exception e) {

                // ignore
                continue;
            }

            boolean neitherSet = (fieldObj == null) && (verifyFieldObj == null);

            if (neitherSet) {
                continue;
            }

            boolean tempMatches = (fieldObj != null) && fieldObj.equals(verifyFieldObj);

            if (!tempMatches) {
                addConstraintViolation(context, fields[i] + " fields do not match", verifyFields[i]);
            }

            matches = matches ? tempMatches : matches;
        }

        return matches;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    private void addConstraintViolation(ConstraintValidatorContext context, String message, String field) {

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addNode(field).addConstraintViolation();
    }
}
