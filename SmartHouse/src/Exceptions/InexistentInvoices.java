package Exceptions;

public class InexistentInvoices extends Exception{
    public InexistentInvoices(String errorMessage) {
        super(errorMessage);
    }
}
