package exceptions;
/**
 *  This exception is thrown when a user inputs an incorrect command.
 * @author Eilam Soroka and Maayan Felig
 */
public class IncorrectCommandException extends InputException {
    public IncorrectCommandException(String message) {
        super(message);
    }
}
