package io.factorialsystems.msscstore21authorization.exception;

public class UserNameAlreadyExistsException extends RuntimeException{
    public UserNameAlreadyExistsException(String msg) {
        super(msg);
    }

    public UserNameAlreadyExistsException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
