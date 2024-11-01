package kr.co.ouoe.User.exception;

public class NoMatchAccountException extends RuntimeException{
    public NoMatchAccountException(String message) {
        super(message);
    }
}
