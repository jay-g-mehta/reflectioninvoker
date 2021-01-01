package reflectioninvoker.exception;


/**
 * Unchecked exception thrown when Target represented as config failed during reading/loading
 */
public class TargetsConfigReadException extends RuntimeException{
    private static final long serialVersionUID = 8161421370546643183L;

    public TargetsConfigReadException(final Throwable cause) {
        super(cause);
    }

    public TargetsConfigReadException() {super();}
}
