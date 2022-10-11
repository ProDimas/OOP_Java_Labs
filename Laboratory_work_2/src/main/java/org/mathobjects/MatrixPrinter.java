package org.mathobjects;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.Locale;

public class MatrixPrinter {
    private final PrintStream output;

    public MatrixPrinter(PrintStream output) {
        this.output = output;
    }

    public PrintStream getOutputStream() {
        return output;
    }

    public void printMatrix(AbstractMatrix toPrint) {
        if (toPrint.getDim().equals(new MatrixDimension(0, 0))) {
            this.output.println("[ ]");
        }
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        formatter.setGroupingUsed(false);
        for (int i = 0; i < toPrint.getDim().rowsCount(); i++) {
            this.output.print("[ ");
            for (int j = 0; j < toPrint.getDim().columnsCount(); j++) {
                this.output.print(formatter.format(toPrint.getElement(i, j)));
                if (j != toPrint.getDim().columnsCount() - 1) {
                    this.output.print(", ");
                }
            }
            this.output.println(" ]");
        }
    }

    public void printlnMatrix(AbstractMatrix toPrint) {
        printMatrix(toPrint);
        System.out.println();
    }
}
