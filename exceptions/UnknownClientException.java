package exceptions;

/**
 * Exception for trying to create an unknown client subclass
 */
public class UnknownClientException extends UnknownDataException {
    @Override
    public String getMessage() {
        return "clients.Client not found!";
    }
}