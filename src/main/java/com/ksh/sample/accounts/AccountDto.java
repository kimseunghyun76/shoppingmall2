package com.ksh.sample.accounts;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by jooyoung on 2015-09-12.
 */
public class AccountDto {

    @Data
    public static class Create{
        @NotEmpty
        @Size(min =5)
        private String userName;

        @NotBlank
        @Size(min =5)
        private String password;
    }
    @Data
    public static class Response {
        private Long id;
        private String userName;
        private String fullName;
        private Date joined;
        private Date updated;
    }
}
