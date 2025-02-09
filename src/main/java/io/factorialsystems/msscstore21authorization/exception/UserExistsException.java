package io.factorialsystems.msscstore21authorization.exception;

public class UserExistsException extends RuntimeException{
    public UserExistsException(String msg) {
        super(msg);
    }
}
