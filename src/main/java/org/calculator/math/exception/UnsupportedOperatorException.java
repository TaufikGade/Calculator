package org.calculator.math.exception;

public class UnsupportedOperatorException extends RuntimeException {
    public UnsupportedOperatorException(String operator) {
        super("Unsupported operator: " + operator);
    }
}