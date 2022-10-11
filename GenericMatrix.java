package org.mathobjects;

import java.util.ArrayList;
import java.util.Objects;

public class GenericMatrix<T extends MatrixElement> {
    private final ArrayList<ArrayList<T>> elements;

    private final MatrixDimension dim;

    public GenericMatrix() {
        this.dim = new MatrixDimension(0, 0);
        this.elements = new ArrayList<>();
    }

    public GenericMatrix(int rowsCount, int columnsCount) {
        if (rowsCount != 0 && columnsCount != 0) {
            this.dim = new MatrixDimension(rowsCount, columnsCount);
        } else {
            this.dim = new MatrixDimension(0, 0);
        }
        this.elements = new ArrayList<>(this.dim.rowsCount());
        for (int i = 0; i < this.dim.rowsCount(); i++) {
            this.elements.set(i, new ArrayList<>(this.dim.columnsCount()));
        }
    }

    public GenericMatrix(GenericMatrix<T> source) {
        Objects.requireNonNull(source);
        this.dim = new MatrixDimension(source.dim.rowsCount(), source.dim.columnsCount());
        this.elements = new ArrayList<>(source.dim.rowsCount());
        for (int i = 0; i < this.dim.rowsCount(); i++) {
            this.elements.set(i, new ArrayList<>(source.dim.columnsCount()));
            for (int j = 0; j < this.dim.columnsCount(); j++) {
                this.elements.get(i).set(i, (T) source.elements.get(i).get(j).clone());
            }
        }
    }
}
