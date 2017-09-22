package org.lokra.weed.error;

import org.lokra.weed.content.ErrorMessage;

public class WeedServerException extends RuntimeException {
    public WeedServerException(ErrorMessage errorMessage) {
        super(errorMessage.getError());
    }
}