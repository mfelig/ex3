package exceptions;
/**
 * A general exception indicating an input error.
 * @author Eilam Soroka and Maayan Felig
 */
public class InputException extends Exception {
    /**
     * Constructs an InputException with the specified detail message.
     * @param message the detail message
     */
    public InputException(String message) {
        super(message);
    }

}
