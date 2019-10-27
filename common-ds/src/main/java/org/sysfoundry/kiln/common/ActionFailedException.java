package org.sysfoundry.kiln.common;

public class ActionFailedException extends Exception{

    public ActionFailedException() {
    }

    public ActionFailedException(String message) {
        super(message);
    }

    public ActionFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActionFailedException(Throwable cause) {
        super(cause);
    }

    public ActionFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
