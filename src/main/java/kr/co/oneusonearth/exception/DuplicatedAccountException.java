package kr.co.oneusonearth.exception;

public class DuplicatedAccountException extends  RuntimeException {
    public  DuplicatedAccountException(String message){
        super(message);
    }
}
