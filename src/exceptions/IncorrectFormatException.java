package exceptions;
/**
 * An exception indicating that some input has an incorrect format.
 * @author Eilam Soroka and Maayan Felig
 */
public class IncorrectFormatException extends InputException {
    /**
     * Constructs an IncorrectFormatException with the specified detail message.
     * @param message the detail message
     */
    public IncorrectFormatException(String message) {
        super(message);
    }
}
