package org.sysfoundry.kiln.factory;

public class ConstructionFailedException extends FactoryException{

    public ConstructionFailedException() {
    }

    public ConstructionFailedException(String message) {
        super(message);
    }

    public ConstructionFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConstructionFailedException(Throwable cause) {
        super(cause);
    }

    public ConstructionFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
