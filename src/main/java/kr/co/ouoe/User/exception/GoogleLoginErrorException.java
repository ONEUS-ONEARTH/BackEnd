package kr.co.ouoe.User.exception;

public class GoogleLoginErrorException extends RuntimeException{
    public GoogleLoginErrorException(String message) {
        super(message);
    }
}
