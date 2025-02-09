package io.factorialsystems.msscstore21authorization.exception;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException(String msg) {
        super(msg);
    }
}
