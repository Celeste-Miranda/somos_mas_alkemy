
package com.alkemy.java.util;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.springframework.stereotype.Component;

@Component
public class UtilValidation {
    
        public void validationApp (Object object){
        
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        
        if (!violations.isEmpty()) {
           throw new ConstraintViolationException(violations);
        }
    }
}
