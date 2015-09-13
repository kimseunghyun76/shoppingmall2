package com.ksh.sample.commons;

import lombok.Data;

import java.util.List;

/**
 * Created by jooyoung on 2015-09-12.
 */
@Data
public class ErrorResponse {

    private String message;
    private String code;

    private List<FieldError> errors;

    public static class FieldError{
        private String field;
        private String value;
        private String reason;
    }
}
