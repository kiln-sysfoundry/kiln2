package org.sysfoundry.kiln.types;

import org.sysfoundry.kiln.factory.FactoryException;

public class UnwrapFailedException extends FactoryException {

    public UnwrapFailedException() {
    }

    public UnwrapFailedException(String message) {
        super(message);
    }

    public UnwrapFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnwrapFailedException(Throwable cause) {
        super(cause);
    }

    public UnwrapFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
