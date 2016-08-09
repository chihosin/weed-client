package org.lokra.seaweedfs.exception;

import java.io.IOException;

/**
 * @author Chiho Sin
 */
public class SeaweedfsException extends IOException {

    public SeaweedfsException(String message) {
        super(message);
    }

    public SeaweedfsException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeaweedfsException(Throwable cause) {
        super(cause);
    }
}
