package io.factorialsystems.msscstore21authorization.exception;

public class AuthorityDoesNotExistException extends RuntimeException{
    public AuthorityDoesNotExistException(String msg) {
        super(msg);
    }

    public AuthorityDoesNotExistException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
