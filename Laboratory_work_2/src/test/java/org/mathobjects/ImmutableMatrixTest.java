package org.mathobjects;

import org.junit.jupiter.api.Test;

import java.util.Random;

class ImmutableMatrixTest {
    Random generator = new Random();

    @Test
    void shouldDenyNegativeDimension() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ImmutableMatrix(-5,-4));
    }

    @Test
    void shouldCreateEmpty() {
        AbstractMatrix m = new ImmutableMatrix();
        org.junit.jupiter.api.Assertions.assertEquals(m.getDim(), new MatrixDimension(0, 0));
    }

    @Test
    void shouldCreateWithDefinedBounds() {
        AbstractMatrix m = new ImmutableMatrix(8, 9);
        org.junit.jupiter.api.Assertions.assertEquals(m.getDim(), new MatrixDimension(8, 9));
    }

    @Test
    void shouldCreateFromArray() {
        int r = 4;
        int c = 14;
        double[][] array = new double[r][c];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                array[i][j] = generator.nextInt(1000);
            }
        }
        AbstractMatrix m = new ImmutableMatrix(array);
        org.junit.jupiter.api.Assertions.assertEquals(m.getDim(), new MatrixDimension(r, c));
    }

    @Test
    void shouldCopyFromImmutable() {
        ImmutableMatrix m = ImmutableMatrix.generate(7,8,40);
        AbstractMatrix t = new ImmutableMatrix(m);
        org.junit.jupiter.api.Assertions.assertTrue(m.equals(t));
        org.junit.jupiter.api.Assertions.assertTrue(t.equals(m));
        org.junit.jupiter.api.Assertions.assertEquals(m.hashCode(), t.hashCode());
    }

    @Test
    void shouldCopyFromMutable() {
        MutableMatrix m = MutableMatrix.generate(3,19, 70);
        AbstractMatrix t = new ImmutableMatrix(m);
        org.junit.jupiter.api.Assertions.assertTrue(m.equals(t));
        org.junit.jupiter.api.Assertions.assertTrue(t.equals(m));
        org.junit.jupiter.api.Assertions.assertEquals(m.hashCode(), t.hashCode());
    }

    @Test
    void shouldFailOnSetElement() {
        AbstractMatrix m = new ImmutableMatrix(new double[][]{{5,4,2,1}});
        org.junit.jupiter.api.Assertions.assertThrows(UnsupportedOperationException.class,
                () -> m.setElement(7,0,2));
    }

    @Test
    void shouldFailOnSetRow() {
        AbstractMatrix m = new ImmutableMatrix(new double[][]{{5,4,2,1}});
        org.junit.jupiter.api.Assertions.assertThrows(UnsupportedOperationException.class,
                () -> m.setRow(new double[]{0,9,3,6},0));
    }

    @Test
    void shouldFailOnSetColumn() {
        AbstractMatrix m = new ImmutableMatrix(new double[][]{{5,4,2,1}});
        org.junit.jupiter.api.Assertions.assertThrows(UnsupportedOperationException.class,
                () -> m.setColumn(new double[]{6},3));
    }

    @Test
    void shouldReturnSameElement() {
        double toCompare = generator.nextDouble();
        double[][] array = new double[10][10];
        array[5][5] = toCompare;
        AbstractMatrix m = new ImmutableMatrix(array);
        org.junit.jupiter.api.Assertions.assertEquals(m.getElement(5, 5), toCompare);
    }

    @Test
    void shouldFailOnInvalidBoundsInGetElement() {
        AbstractMatrix m = ImmutableMatrix.generate(2,2,65);
        org.junit.jupiter.api.Assertions.assertThrows(IndexOutOfBoundsException.class,
                ()->m.getElement(5, 1));
        org.junit.jupiter.api.Assertions.assertThrows(IndexOutOfBoundsException.class,
                ()->m.getElement(1, -3));
    }

    @Test
    void shouldReturnSameRow() {
        double[] toCompare = new double[10];
        for (int i = 0; i < 10; i++) {
            toCompare[i] = generator.nextDouble();
        }
        double[][] array = new double[10][10];
        array[2] = toCompare;
        AbstractMatrix m = new ImmutableMatrix(array);
        double[] fromM = m.getRow(2);
        for (int i = 0; i < 10; i++) {
            org.junit.jupiter.api.Assertions.assertEquals(fromM[i], toCompare[i]);
        }
    }

    @Test
    void shouldFailOnInvalidBoundsInGetRow() {
        AbstractMatrix m = ImmutableMatrix.generate(2,2,65);
        org.junit.jupiter.api.Assertions.assertThrows(IndexOutOfBoundsException.class,
                ()->m.getRow(9));
        org.junit.jupiter.api.Assertions.assertThrows(IndexOutOfBoundsException.class,
                ()->m.getRow(-1));
    }

    @Test
    void shouldReturnSameColumn() {
        double[] toCompare = new double[6];
        for (int i = 0; i < 6; i++) {
            toCompare[i] = generator.nextDouble();
        }
        double[][] array = new double[6][8];
        for (int i = 0; i < 6; i++) {
            array[i][4] = toCompare[i];
        }
        AbstractMatrix m = new ImmutableMatrix(array);
        double[] fromM = m.getColumn(4);
        for (int i = 0; i < 6; i++) {
            org.junit.jupiter.api.Assertions.assertEquals(fromM[i], toCompare[i]);
        }
    }

    @Test
    void shouldFailOnInvalidBoundsInGetColumn() {
        AbstractMatrix m = ImmutableMatrix.generate(2,2,65);
        org.junit.jupiter.api.Assertions.assertThrows(IndexOutOfBoundsException.class,
                ()->m.getColumn(105));
        org.junit.jupiter.api.Assertions.assertThrows(IndexOutOfBoundsException.class,
                ()->m.getColumn(-31));
    }

    @Test
    void shouldCreateFromDiagonal() {
        double[] d = new double[]{5,12,54,125,1,5,7347,31,0};
        int r = d.length;
        int c = d.length;
        AbstractMatrix m = ImmutableMatrix.fromDiagonal(d);
        org.junit.jupiter.api.Assertions.assertEquals(m.getDim(), new MatrixDimension(r, c));
    }

    @Test
    void shouldMultiply() {
        AbstractMatrix m1 = new ImmutableMatrix(new double[][]{
                {0, 0, 10},
                {5, 5, 0}
        });
        AbstractMatrix m2 = new ImmutableMatrix(new double[][]{
                {5, 0, 0},
                {10, 0, 10},
                {0, 5, 100}
        });
        AbstractMatrix expected = new ImmutableMatrix(new double[][]{
                {0, 50, 1000},
                {75, 0, 50}
        });
        org.junit.jupiter.api.Assertions.assertTrue(expected.equals(m1.multiply(m2)));
    }

    @Test
    void shouldFailOnNonCompatibleMatrices() {
        AbstractMatrix m1 = ImmutableMatrix.generate(5, 10, 100);
        AbstractMatrix m2 = ImmutableMatrix.generate(20,5, 100);
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()->m1.multiply(m2));
    }

    @Test
    void shouldHaveTheSameReferencesWhenCopyingFromImmutable() {
        AbstractMatrix m = ImmutableMatrix.generate(10,15,5);
        AbstractMatrix copy = new ImmutableMatrix(m);
        org.junit.jupiter.api.Assertions.assertEquals(m.getElementsRef(), copy.getElementsRef());
    }
}