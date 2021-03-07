package exceptions;

/**
 * Exception for a faulty JSON file in which all the parameters
 * cannot be read
 */
public class JSONDataException extends Exception {

    @Override
    public String getMessage() {
        return "Bad JSON";
    }
}
