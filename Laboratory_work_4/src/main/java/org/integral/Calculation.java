package org.integral;

import org.springframework.lang.NonNull;

import java.util.Objects;

public record Calculation(String func, double start, double end, double step, double result, int id) {
    public Calculation(@NonNull String func, double start, double end, double step, double result, int id) {
        Objects.requireNonNull(func, "func representation must be non null");
        if (!(start < end)) {
            throw new IllegalArgumentException("start must be less then end");
        }

        if (step <= 0) {
            throw new IllegalArgumentException("step must be positive");
        }

        if (id < 0) {
            throw new IllegalArgumentException("id to save calculations must be positive or 0");
        }

        this.func = func;
        this.start = start;
        this.end = end;
        this.step = step;
        this.result = result;
        this.id = id;
    }
}
