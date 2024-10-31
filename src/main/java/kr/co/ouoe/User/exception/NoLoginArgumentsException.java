package kr.co.ouoe.User.exception;

public class NoLoginArgumentsException extends RuntimeException{
    public NoLoginArgumentsException(String message) {
        super(message);
    }
}
