package interfaces;
import clients.ClientFactory;
import exceptions.UnknownDataException;
import products.ProductFactory;

/**
 * Class for providing a specific factory implementation
 * for an object
 */
public class FactoryProvider {

    /**
     * private constructor to prevent instantiation
     */
    private FactoryProvider() {
    }

    /**
     * Returns either a client or a product factory for creating subtypes of these objects
     * @param type string representing the name of the class of the object of whom the subtypes are created
     * @return a concrete factory
     * @throws UnknownDataException if the demanded object is invalid
     */
    @SuppressWarnings("rawtypes")
    public static AbstractFactory getFactory(String type) throws UnknownDataException {
        if("client".equals(type)) {
            return new ClientFactory();
        }
        else if ("product".equals(type)) {
            return new ProductFactory();
        }
        else {
            throw new UnknownDataException();
        }
    }
}