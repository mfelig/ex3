package exceptions;
/**
 * An exception indicating that some input is out of the allowed boundaries.
 * @author Eilam Soroka and Maayan Felig
 */
public class BoundariesException extends InputException {
    /**
     * Constructs a BoundariesException with the specified detail message.
     * @param message the detail message
     */
    public BoundariesException(String message) {
        super(message);
    }
}
