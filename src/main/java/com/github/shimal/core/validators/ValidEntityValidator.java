
package com.github.shimal.core.validators;


import com.github.shimal.core.constraints.ValidEntity;
import com.github.shimal.core.daos.GenericDAO;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;



public class ValidEntityValidator implements ConstraintValidator<ValidEntity, Object> {



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    @Autowired
    private GenericDAO            dao;
    private boolean               nullable;



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    @Override
    public void initialize(ValidEntity constraintAnnotation) {

        this.nullable = constraintAnnotation.nullable();
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    public boolean isValid(Object entity, ConstraintValidatorContext context) {

        try {

            if (nullable && entity == null) {
                return true;
            }

            Class      entityClass = entity.getClass();
            Annotation entityAnno  = entityClass.getAnnotation(Entity.class);

            if (entityAnno == null) {
                return false;
            }

            Method getter = searchIdGetter(entityClass);

            if (getter == null) {
                return false;
            }

            Serializable idValue = (Serializable) getter.invoke(entity);

            if (!nullable && idValue == null) {
                context.buildConstraintViolationWithTemplate("Alan boş bırakılamaz!").addConstraintViolation();
                context.disableDefaultConstraintViolation();

                return false;
            }

            if (nullable && idValue == null) {
                return true;
            }

            if (dao.get(entityClass, idValue) == null) {
                return false;
            }

            return true;
        } catch (Exception e) {
        }

        return false;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    private Method searchIdGetter(Class entityClass) throws NoSuchMethodException {

        Field[] fields  = entityClass.getDeclaredFields();
        Field   idField = null;

        for (Field field : fields) {

            if (field.getAnnotation(Id.class) != null) {

                idField = field;

                break;
            }
        }

        if (idField != null) {

            String fieldName      = idField.getName();
            String upperFieldName = String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);

            try {
                return entityClass.getDeclaredMethod("get" + upperFieldName);
            } catch (NoSuchMethodException ex) {

                if (idField.getType().equals(Boolean.class)) {

                    try {
                        return entityClass.getDeclaredMethod("is" + upperFieldName);
                    } catch (NoSuchMethodException e) {

                    }
                }
            }
        } else {
            Method[] methods = entityClass.getDeclaredMethods();

            for (Method method : methods) {

                if (method.getName().startsWith("get") && method.getAnnotation(Id.class) != null) {

                    return method;
                }
            }
        }

        return null;
    }
}
