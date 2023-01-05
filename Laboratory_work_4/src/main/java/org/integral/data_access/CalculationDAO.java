package org.integral.data_access;

import org.integral.Calculation;

public interface CalculationDAO {
    void write(Calculation calc);

    Calculation read(int id);
}
