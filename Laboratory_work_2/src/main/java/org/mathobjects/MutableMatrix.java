package org.mathobjects;

import java.util.Objects;
import java.util.Random;

public class MutableMatrix extends AbstractMatrix {
    private final double[][] elements;

    private final MatrixDimension dim;

    public MutableMatrix() {
        this.dim = new MatrixDimension(0, 0);
        this.elements = new double[0][0];
    }

    public MutableMatrix(int rowsCount, int columnsCount) {
        if (rowsCount != 0 && columnsCount != 0) {
            this.dim = new MatrixDimension(rowsCount, columnsCount);
        } else {
            this.dim = new MatrixDimension(0, 0);
        }
        this.elements = new double[this.dim.rowsCount()][this.dim.columnsCount()];
    }

    public MutableMatrix(AbstractMatrix source) {
        Objects.requireNonNull(source);
        this.dim = new MatrixDimension(source.getDim().rowsCount(), source.getDim().columnsCount());
        this.elements = new double[this.dim.rowsCount()][this.dim.columnsCount()];
        for (int i = 0; i < this.dim.rowsCount(); i++) {
            this.elements[i] = source.getRow(i);
        }
    }

    public MutableMatrix(double[][] array) {
        this.checkArrayForm(array);
        if (array.length == 0) {
            this.dim = new MatrixDimension(0, 0);
        } else if (array[0].length == 0) {
            this.dim = new MatrixDimension(0, 0);
        } else {
            this.dim = new MatrixDimension(array.length, array[0].length);
        }
        this.elements = new double[this.dim.rowsCount()][this.dim.columnsCount()];
        for (int i = 0; i < this.dim.rowsCount(); i++) {
            System.arraycopy(array[i], 0, this.elements[i], 0, this.dim.columnsCount());
        }
    }

    @Override
    public MatrixDimension getDim() {
        return this.dim;
    }

    @Override
    protected double[][] getElementsRef() {
        return this.elements;
    }

    @Override
    public void setRow(double[] rowToAdd, int row) {
        Objects.requireNonNull(rowToAdd);
        if (rowToAdd.length != 0) {
            this.dim.fitsInBounds(row, rowToAdd.length);
            System.arraycopy(rowToAdd, 0, this.elements[row], 0, rowToAdd.length);
        }
    }

    @Override
    public void setColumn(double[] columnToAdd, int column) {
        Objects.requireNonNull(columnToAdd);
        if (columnToAdd.length != 0) {
            this.dim.fitsInBounds(columnToAdd.length, column);
            for (int i = 0; i < columnToAdd.length; i++) {
                this.elements[i][column] = columnToAdd[i];
            }
        }
    }

    @Override
    public void setElement(double value, int row, int column) {
        this.dim.fitsInBounds(row, column);
        this.elements[row][column] = value;
    }

    @Override
    public MutableMatrix multiply(AbstractMatrix operand) {
        Objects.requireNonNull(operand);
        return new MutableMatrix(internalMultiply(operand));
    }

    public static MutableMatrix fromDiagonal(double[] diagonal) {
        Objects.requireNonNull(diagonal);
        MutableMatrix diagonalMatrix = new MutableMatrix(diagonal.length, diagonal.length);
        for (int i = 0; i < diagonal.length; i++) {
            diagonalMatrix.setElement(diagonal[i], i, i);
        }
        return diagonalMatrix;
    }

    public static MutableMatrix generate(int rowsCount, int columnsCount, int upperBound) {
        Random generator = new Random();
        MutableMatrix randomMatrix = new MutableMatrix(rowsCount, columnsCount);
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                randomMatrix.setElement(generator.nextDouble() * generator.nextInt(upperBound), i, j);
            }
        }
        return randomMatrix;
    }
}
