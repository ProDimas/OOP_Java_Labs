package org.mathobjects;

public record MatrixDimension(int rowsCount, int columnsCount) {
    public MatrixDimension {
        if (rowsCount < 0) {
            throw new IllegalArgumentException(String.valueOf(rowsCount).concat(" < 0"));
        }
        if (columnsCount < 0) {
            throw new IllegalArgumentException(String.valueOf(columnsCount).concat(" < 0"));
        }
    }

    public void fitsInBounds(int rowsToCheck, int columnsToCheck) {
        if (rowsToCheck < 0 || rowsToCheck > this.rowsCount) {
            String rS = String.valueOf(rowsToCheck);
            String rD = String.valueOf(this.rowsCount);
            throw new IndexOutOfBoundsException(rS.concat(" not in ").concat("0..").concat(rD));
        }
        if (columnsToCheck < 0 || columnsToCheck > this.columnsCount) {
            String cS = String.valueOf(columnsToCheck);
            String cD = String.valueOf(this.columnsCount);
            throw new IndexOutOfBoundsException(cS.concat(" not in ").concat("0..").concat(cD));
        }
        if (this.rowsCount == 0 || this.columnsCount == 0) {
            throw new IndexOutOfBoundsException("no access within 0..0");
        }
    }
}
