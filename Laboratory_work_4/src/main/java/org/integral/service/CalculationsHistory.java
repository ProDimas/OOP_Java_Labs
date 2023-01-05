package org.integral.service;

import org.integral.Calculation;
import org.integral.data_access.CalculationDAO;
import org.integral.data_access.CalculationTxtDAO;
import org.springframework.lang.NonNull;

import java.util.Objects;

public class CalculationsHistory {
    private static final CalculationDAO calcDAO = new CalculationTxtDAO();

    public static Calculation getById(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("id to get calculations must be positive or 0");
        }

        return calcDAO.read(id);
    }

    static void save(@NonNull Calculation calc) {
        Objects.requireNonNull(calc, "calc must be non null");
        calcDAO.write(calc);
    }
}
