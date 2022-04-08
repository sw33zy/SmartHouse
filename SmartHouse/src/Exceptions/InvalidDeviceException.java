package Exceptions;

public class InvalidDeviceException extends Exception{
    public InvalidDeviceException(String errorMessage) {
        super(errorMessage);
    }
}
