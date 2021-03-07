package exceptions;

/**
 * Exception for invalid requests for a product,
 * for example, the client can not bid enough
 */
public class InvalidProductRequest extends Exception {

    @Override
    public String getMessage() {
        return "Maximum bid is too small for this product!";
    }
}
