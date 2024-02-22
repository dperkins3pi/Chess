package handler;

public class AlreadyTakenException extends Exception{
    public AlreadyTakenException(String s) {
        super(s);
    }
}
