package org.integral.service;

import org.integral.Calculation;
import org.integral.IntegrationTaskInfo;
import org.springframework.lang.NonNull;

public class Calculator {
    public static void calculate(@NonNull IntegrationTaskInfo info, int id) {
        if (id < 0) {
            throw new IllegalArgumentException("id to save calculations must be positive or 0");
        }

        var f = info.function();
        double result;
        if (info.getEnd() - info.getStart() <= info.getStep()) {
            result = (f.apply(info.getStart()) + f.apply(info.getEnd())) / 2;
        } else {
            result = f.apply(info.getStart()) / 2;
            double start = info.getStart() + info.getStep();
            while (start < info.getEnd()) {
                result += f.apply(start);
                start += info.getStep();
            }
            result -= f.apply(start - info.getStep()) / 2;
            result *= info.getStep();
            result += ((f.apply(start - info.getStep()) + f.apply(info.getEnd())) / 2) * (info.getEnd() - start);
        }
        Calculation calc = new Calculation(info.getStringFunction(), info.getStart()
                , info.getEnd(), info.getStep(), result, id);
        CalculationsHistory.save(calc);
    }
}
