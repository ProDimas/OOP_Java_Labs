package org.mathobjects;

import java.util.Arrays;
import java.util.Objects;

public abstract class AbstractMatrix {
    abstract public void setRow(double[] rowToAdd, int row);

    abstract public void setColumn(double[] columnToAdd, int column);

    abstract public void setElement(double value, int row, int column);

    abstract public MatrixDimension getDim();

    abstract protected double[][] getElementsRef();

    public double[] getRow(int row) {
        this.getDim().fitsInBounds(row, 0);
        return Arrays.copyOf(this.getElementsRef()[row], this.getDim().columnsCount());
    }

    public double[] getColumn(int column) {
        this.getDim().fitsInBounds(0, column);
        double[] columnToReturn = new double[this.getDim().rowsCount()];
        for (int i = 0; i < this.getDim().rowsCount(); i++) {
            columnToReturn[i] = this.getElementsRef()[i][column];
        }
        return columnToReturn;
    }

    public double getElement(int row, int column) {
        this.getDim().fitsInBounds(row, column);
        return this.getElementsRef()[row][column];
    }

    abstract public AbstractMatrix multiply(AbstractMatrix operand);

    protected double[][] internalMultiply(AbstractMatrix operand) {
        boolean checkFirstOperand = this.getDim().rowsCount() == 0 || this.getDim().columnsCount() == 0;
        boolean checkSecondOperand = operand.getDim().rowsCount() == 0 || operand.getDim().columnsCount() == 0;
        if (checkFirstOperand || checkSecondOperand) {
            throw new IllegalArgumentException("invalid operand: empty matrix");
        }
        if (this.getDim().columnsCount() != operand.getDim().rowsCount()) {
            throw new IllegalArgumentException("not compatible matrices");
        }
        double[][] array = new double[this.getDim().rowsCount()][operand.getDim().columnsCount()];
        for (int i = 0; i < this.getDim().rowsCount(); i++) {
            for (int j = 0; j < operand.getDim().columnsCount(); j++) {
                double toAdd = 0;
                for (int k = 0; k < this.getDim().columnsCount(); k++) {
                    toAdd += this.getElement(i, k) * operand.getElement(k, j);
                }
                array[i][j] = toAdd;
            }
        }
        return array;
    }

    protected void checkArrayForm(double[][] arrayToCheck) {
        Objects.requireNonNull(arrayToCheck);
        int length = 0;
        for (int i = 0; i < arrayToCheck.length; i++) {
            arrayToCheck[i] = Objects.requireNonNull(arrayToCheck[i]);
            if (i == 0) {
                length = arrayToCheck[i].length;
            } else if (arrayToCheck[i].length != length) {
                String a = String.valueOf(arrayToCheck[i].length);
                String l = String.valueOf(length);
                throw new IllegalArgumentException(a.concat(" != ").concat(l));
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractMatrix)) {
            return false;
        }
        if (!(((AbstractMatrix) obj).getDim().equals(this.getDim()))) {
            return false;
        }
        for (int i = 0; i < this.getDim().rowsCount(); i++) {
            for (int j = 0; j < this.getDim().columnsCount(); j++) {
                if (((AbstractMatrix) obj).getElement(i, j) != this.getElement(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int sum = 0;
        for (int i = 0; i < this.getDim().rowsCount(); i++) {
            for (int j = 0; j < this.getDim().columnsCount(); j++) {
                sum += (int) this.getElement(i, j);
            }
        }
        if (this.getDim().rowsCount() < this.getDim().columnsCount()) {
            sum += sum % 2;
        } else if (this.getDim().rowsCount() > this.getDim().columnsCount()) {
            sum += (sum + 1) % 2;
        }
        return sum;
    }
}
