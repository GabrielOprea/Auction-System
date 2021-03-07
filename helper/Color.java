package helper;

/**
 * Utility class that provides string constants used for
 * producing a colored output in the terminal
 */
@SuppressWarnings("unused")
public class Color {

    /**
     * private constructor that does not allow instantiation
     */
    private Color() {
    }

    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
}
