package Exceptions;

public class InvalidRoomException extends Exception{
    public InvalidRoomException(String errorMessage) {
        super(errorMessage);
    }
}
