package reflectioninvoker.exception;

/**
 * Unchecked exception thrown when failure in invoking method using Reflection
 */
public class ReflectionInvokeException extends RuntimeException {
    private static final long serialVersionUID = 5541930540919571202L;

    public ReflectionInvokeException() {super();}

    public ReflectionInvokeException(final Throwable cause) {super(cause);}

    public ReflectionInvokeException(final String message, final Throwable cause) {super(message, cause);}
}
