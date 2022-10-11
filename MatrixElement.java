package org.mathobjects;

public interface MatrixElement {
    MatrixElement multiply(MatrixElement operand);

    MatrixElement add(MatrixElement operand);

    boolean equals(MatrixElement operand);

    MatrixElement clone();
}
