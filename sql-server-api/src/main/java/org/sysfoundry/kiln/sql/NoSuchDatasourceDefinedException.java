package org.sysfoundry.kiln.sql;

public class NoSuchDatasourceDefinedException extends SQLServerException {

    public NoSuchDatasourceDefinedException() {
    }

    public NoSuchDatasourceDefinedException(String message) {
        super(message);
    }

    public NoSuchDatasourceDefinedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchDatasourceDefinedException(Throwable cause) {
        super(cause);
    }

    public NoSuchDatasourceDefinedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
