package com.echameunapata.backend.utils.errors;

import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ErrorTools {

    public Map<String, List<String>> mapErrors(List<FieldError>errors){
        Map<String, List<String>> errorMap = new HashMap<>();

        errors.forEach(error->{
            List<String> _errors = errorMap
                    .getOrDefault(error.getField(), new ArrayList<>());
            _errors.add(error.getDefaultMessage());
            errorMap.put(error.getField(), _errors);
        });

        return errorMap;
    }
}
