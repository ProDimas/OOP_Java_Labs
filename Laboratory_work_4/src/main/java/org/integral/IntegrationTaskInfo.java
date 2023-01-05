package org.integral;

import java.util.function.Function;

public final class IntegrationTaskInfo {
    private final String stringFunction = "sin(x)";

    private final Function<Double, Double> function = Math::sin;

    private final double start;

    private final double end;

    private final double step;

    public IntegrationTaskInfo(double start, double end, double step) {
        if (!(start < end)) {
            throw new IllegalArgumentException("start must be less then end");
        }

        if (step <= 0) {
            throw new IllegalArgumentException("step must be positive");
        }

        this.start = start;
        this.end = end;
        this.step = step;
    }

    public Function<Double, Double> function() {
        return this.function;
    }

    public String getStringFunction() { return this.stringFunction; }

    public double getStart() { return this.start; }

    public double getEnd() { return this.end; }

    public double getStep() { return this.step; }
}
