package org.sysfoundry.kiln.sql;

public class SQLServerException extends Exception{

    public SQLServerException() {
    }

    public SQLServerException(String message) {
        super(message);
    }

    public SQLServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLServerException(Throwable cause) {
        super(cause);
    }

    public SQLServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
