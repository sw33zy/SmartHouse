package Exceptions;

public class InvalidSupplierException extends Exception{
    public InvalidSupplierException(String errorMessage) {
        super(errorMessage);
    }
}
