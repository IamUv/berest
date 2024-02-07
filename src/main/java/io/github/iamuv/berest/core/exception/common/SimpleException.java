package io.github.iamuv.berest.core.exception.common;

import io.github.iamuv.berest.core.exception.HttpStatusCode;
import jakarta.servlet.http.HttpServletResponse;

public class SimpleException extends Exception implements HttpStatusCode {

    protected int httpStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

    public SimpleException() {
        super();
    }

    public SimpleException(int httpStatusCode) {
        super();
        this.httpStatusCode = httpStatusCode;
    }

    public SimpleException(String message) {
        super(message);
    }

    public SimpleException(String message, int httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public SimpleException(String message, Throwable cause, int httpStatusCode) {
        super(message, cause);
        this.httpStatusCode = httpStatusCode;
    }

    public SimpleException(Throwable cause, int httpStatusCode) {
        super(cause);
        this.httpStatusCode = httpStatusCode;
    }

    public SimpleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int httpStatusCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
