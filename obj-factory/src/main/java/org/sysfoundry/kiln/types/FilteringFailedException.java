package org.sysfoundry.kiln.types;

import org.sysfoundry.kiln.factory.FactoryException;

public class FilteringFailedException extends FactoryException {

    public FilteringFailedException() {
    }

    public FilteringFailedException(String message) {
        super(message);
    }

    public FilteringFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public FilteringFailedException(Throwable cause) {
        super(cause);
    }

    public FilteringFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
