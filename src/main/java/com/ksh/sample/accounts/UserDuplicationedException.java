package com.ksh.sample.accounts;

/**
 * Created by jooyoung on 2015-09-12.
 */
class UserDuplicatedException extends RuntimeException {

    String username;

    public UserDuplicatedException(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }
}
