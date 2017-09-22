package org.lokra.weed.error;

import org.lokra.weed.content.ErrorMessage;

public class WeedFileException extends RuntimeException {
    public WeedFileException(ErrorMessage errorMessage) {
        super(errorMessage.getError());
    }
}
