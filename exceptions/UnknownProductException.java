package exceptions;

/**
 * Exception for trying to create an unknown subtype of a product
 */
public class UnknownProductException extends UnknownDataException {

    @Override
    public String getMessage() {
        return "products.Product not found!";
    }
}
