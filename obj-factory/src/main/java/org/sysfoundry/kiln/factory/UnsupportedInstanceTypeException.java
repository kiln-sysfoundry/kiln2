package org.sysfoundry.kiln.factory;

public class UnsupportedInstanceTypeException extends FactoryException{

    public UnsupportedInstanceTypeException() {
    }

    public UnsupportedInstanceTypeException(String message) {
        super(message);
    }

    public UnsupportedInstanceTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedInstanceTypeException(Throwable cause) {
        super(cause);
    }

    public UnsupportedInstanceTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
