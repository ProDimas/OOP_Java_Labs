package org.fpm.di;

public class CyclicDependencyException extends RuntimeException {
    CyclicDependencyException(String message) {
        super(message);
    }
}

