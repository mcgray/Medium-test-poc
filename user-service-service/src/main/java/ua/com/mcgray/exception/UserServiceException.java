package ua.com.mcgray.exception;

/**
 * @author orezchykov
 * @since 04.12.14
 */
public class UserServiceException extends RuntimeException {
    public UserServiceException() {
    }

    public UserServiceException(final String message) {
        super(message);
    }

    public UserServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserServiceException(final Throwable cause) {
        super(cause);
    }

    public UserServiceException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
