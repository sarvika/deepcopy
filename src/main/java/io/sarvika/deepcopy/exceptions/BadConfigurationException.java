package io.sarvika.deepcopy.exceptions;

public class BadConfigurationException extends IllegalArgumentException {
    public BadConfigurationException() {}

    public BadConfigurationException(String s) {
        super(s);
    }

    public BadConfigurationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public BadConfigurationException(Throwable throwable) {
        super(throwable);
    }
}
