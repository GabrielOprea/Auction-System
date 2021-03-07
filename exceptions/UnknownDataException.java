package exceptions;

/**
 * Exception for trying to create an unknown subtype for an object
 */
public class UnknownDataException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid data!";
    }
}