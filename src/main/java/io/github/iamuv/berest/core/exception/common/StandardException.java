package io.github.iamuv.berest.core.exception.common;

import io.github.iamuv.berest.core.exception.ErrorDescription;
import io.github.iamuv.berest.core.exception.StandardCode;

public class StandardException extends SimpleException implements StandardCode, ErrorDescription {

    protected String code;

    protected String description;

    public StandardException(String code, String description) {
        super();
        this.code = code;
        this.description = description;
    }

    public StandardException(String message, String code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public StandardException(int httpStatusCode, String code, String description) {
        super(httpStatusCode);
        this.code = code;
        this.description = description;
    }

    public StandardException(String message, int httpStatusCode, String code, String description) {
        super(message, httpStatusCode);
        this.code = code;
        this.description = description;
    }

    public StandardException(String message, Throwable cause, int httpStatusCode, String code, String description) {
        super(message, cause, httpStatusCode);
        this.code = code;
        this.description = description;
    }

    public StandardException(Throwable cause, int httpStatusCode, String code, String description) {
        super(cause, httpStatusCode);
        this.code = code;
        this.description = description;
    }

    public StandardException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int httpStatusCode, String code, String description) {
        super(message, cause, enableSuppression, writableStackTrace, httpStatusCode);
        this.code = code;
        this.description = description;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }


}
