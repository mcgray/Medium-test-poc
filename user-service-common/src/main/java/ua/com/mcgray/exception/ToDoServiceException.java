package ua.com.mcgray.exception;

/**
 * @author orezchykov
 * @since 04.12.14
 */
public class ToDoServiceException extends RuntimeException {

    public ToDoServiceException() {
    }

    public ToDoServiceException(final String message) {
        super(message);
    }

    public ToDoServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ToDoServiceException(final Throwable cause) {
        super(cause);
    }

    public ToDoServiceException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
