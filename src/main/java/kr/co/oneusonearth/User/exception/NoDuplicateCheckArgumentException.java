package kr.co.oneusonearth.user.exception;

public class NoDuplicateCheckArgumentException extends RuntimeException{
    public NoDuplicateCheckArgumentException(String message) {
        super(message);
    }
}