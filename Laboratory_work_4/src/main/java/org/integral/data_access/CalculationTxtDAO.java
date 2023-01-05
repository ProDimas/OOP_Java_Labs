package org.integral.data_access;

import org.integral.Calculation;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CalculationTxtDAO implements CalculationDAO {
    private final String fileName = "calculations.txt";

    public final List<Integer> registeredIds;

    public CalculationTxtDAO() {
        this.registeredIds = new ArrayList<>();
        try {
            FileReader reader = new FileReader(fileName);
            Scanner scanner = new Scanner(reader);
            while (scanner.hasNext()) {
                String s = scanner.next();
                if (s.startsWith("#")) {
                    this.registeredIds.add(Integer.parseInt(s.substring(1)));
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(Calculation calc) {
        if (registeredIds.contains(calc.id())) {
            throw new RuntimeException("Requested id already present");
        }
        try (Writer writer = new FileWriter(fileName, true)) {
            registeredIds.add(calc.id());
            String idHead = "#".concat(String.valueOf(calc.id()));
            writer.write(idHead);
            writer.write('\n');
            Arrays.stream(getStrRepresentationOfCalculation(calc)).forEach(str -> {
                try {
                    writer.write(" ".repeat(idHead.length()));
                    writer.write(str);
                    writer.write('\n');
                } catch (IOException e) {
                    try {
                        writer.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String[] getStrRepresentationOfCalculation(Calculation calc) {
        final int fieldsAmount = 5;
        String[] representation = new String[fieldsAmount];
        representation[0] = calc.func();
        representation[1] = String.valueOf(calc.start());
        representation[2] = String.valueOf(calc.end());
        representation[3] = String.valueOf(calc.step());
        representation[4] = String.valueOf(calc.result());
        return representation;
    }

    private Calculation convertStringsToCalculationWith(Scanner scanner, int id) {
        String func = scanner.next();
        double start = Double.parseDouble(scanner.next());
        double end = Double.parseDouble(scanner.next());
        double step = Double.parseDouble(scanner.next());
        double result = Double.parseDouble(scanner.next());
        return new Calculation(func, start, end, step, result, id);
    }

    @Override
    public Calculation read(int id) {
        try {
            FileReader reader = new FileReader(fileName);
            Scanner scanner = new Scanner(reader);
            while (scanner.hasNext()) {
                String s = scanner.next();
                if (s.startsWith("#")) {
                    if (Integer.parseInt(s.substring(1)) == id) {
                        return convertStringsToCalculationWith(scanner, id);
                    }
                }
            }
            throw new RuntimeException("No such id present");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
