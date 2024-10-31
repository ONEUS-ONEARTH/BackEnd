package kr.co.ouoe.User.exception;

public class NoDuplicateCheckArgumentException extends RuntimeException{
    public NoDuplicateCheckArgumentException(String message) {
        super(message);
    }
}