package ua.com.mcgray.exception;

/**
 * @author orezchykov
 * @since 07.02.15
 */
public class AccountServiceException extends RuntimeException {

    public AccountServiceException() {
    }

    public AccountServiceException(final String message) {
        super(message);
    }

    public AccountServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AccountServiceException(final Throwable cause) {
        super(cause);
    }

    public AccountServiceException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
