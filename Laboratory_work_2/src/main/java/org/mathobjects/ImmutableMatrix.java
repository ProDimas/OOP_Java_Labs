package org.mathobjects;

import java.util.Objects;
import java.util.Random;

public final class ImmutableMatrix extends AbstractMatrix {
    private final double[][] elements;

    private final MatrixDimension dim;

    public ImmutableMatrix() {
        this.dim = new MatrixDimension(0, 0);
        this.elements = new double[0][0];
    }

    public ImmutableMatrix(int rowsCount, int columnsCount) {
        if (rowsCount != 0 && columnsCount != 0) {
            this.dim = new MatrixDimension(rowsCount, columnsCount);
        } else {
            this.dim = new MatrixDimension(0, 0);
        }
        this.elements = new double[this.dim.rowsCount()][this.dim.columnsCount()];
    }

    public ImmutableMatrix(AbstractMatrix source) {
        Objects.requireNonNull(source);
        if (source instanceof ImmutableMatrix) {
            this.dim = source.getDim();
            this.elements = ((ImmutableMatrix) source).elements;
        } else {
            this.dim = new MatrixDimension(source.getDim().rowsCount(), source.getDim().columnsCount());
            this.elements = new double[this.dim.rowsCount()][this.dim.columnsCount()];
            for (int i = 0; i < this.dim.rowsCount(); i++) {
                this.elements[i] = source.getRow(i);
            }
        }
    }

    public ImmutableMatrix(double[][] array) {
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
        throw new UnsupportedOperationException("not allowed in immutable object");
    }

    @Override
    public void setColumn(double[] columnToAdd, int column) {
        throw new UnsupportedOperationException("not allowed in immutable object");
    }

    @Override
    public void setElement(double value, int row, int column) {
        throw new UnsupportedOperationException("not allowed in immutable object");
    }

    public static ImmutableMatrix generate(int rowsCount, int columnsCount, int upperBound) {
        Random generator = new Random();
        double[][] basis = new double[rowsCount][columnsCount];
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                basis[i][j] = generator.nextDouble() * generator.nextInt(upperBound);
            }
        }
        return new ImmutableMatrix(basis);
    }

    public static ImmutableMatrix fromDiagonal(double[] diagonal) {
        Objects.requireNonNull(diagonal);
        double[][] array = new double[diagonal.length][diagonal.length];
        for (int i = 0; i < diagonal.length; i++) {
            array[i][i] = diagonal[i];
        }
        return new ImmutableMatrix(array);
    }

    @Override
    public ImmutableMatrix multiply(AbstractMatrix operand) {
        Objects.requireNonNull(operand);
        return new ImmutableMatrix(this.internalMultiply(operand));
    }
}
