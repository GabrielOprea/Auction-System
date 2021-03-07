package exceptions;

/**
 * Exception for duplicated requests for the same product, by the
 * same client
 */
public class DuplicateRequestException extends Exception {

    @Override
    public String getMessage() {
        return "Duplicate request for this client and product!";
    }
}
