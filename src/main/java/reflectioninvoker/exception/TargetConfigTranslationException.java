package reflectioninvoker.exception;


/**
 * Unchecked exception thrown when Target represented in config was failed to translate during deserialization
 */
public class TargetConfigTranslationException extends RuntimeException {
    private static final long serialVersionUID = -709136970683296628L;

    public TargetConfigTranslationException(final Throwable throwable) {
        super(throwable);
    }

    public TargetConfigTranslationException() {super();}
}
