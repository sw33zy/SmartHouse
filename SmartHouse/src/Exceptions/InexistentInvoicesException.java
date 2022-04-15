package Exceptions;

public class InexistentInvoicesException extends Exception{
    public InexistentInvoicesException(String errorMessage) {
        super(errorMessage);
    }
}
