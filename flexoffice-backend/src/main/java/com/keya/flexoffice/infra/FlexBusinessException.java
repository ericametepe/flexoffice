package com.keya.flexoffice.infra;

public class FlexBusinessException extends RuntimeException {

    public FlexBusinessException() {
    }

    public FlexBusinessException(String ...message) {
        super(String.join(",", message));

    }

    public FlexBusinessException(String message) {
        super(message);
    }
    public FlexBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
