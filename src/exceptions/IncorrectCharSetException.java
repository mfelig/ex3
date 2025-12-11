package exceptions;
/**
 * An exception indicating that some input contains incorrect characters.
 * @author Eilam Soroka and Maayan Felig
 */
public class IncorrectCharSetException extends InputException {
    /**
     * Constructs an IncorrectCharSetException with the specified detail message.
     * @param message the detail message
     */
    public IncorrectCharSetException(String message) {
        super(message);
    }
}
