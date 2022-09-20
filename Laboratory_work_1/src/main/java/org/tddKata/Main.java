package org.tddKata;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.print("Enter a string to parse(newline to end an input): ");
        Scanner input = new Scanner(System.in);
        String numbers = "";
        numbers = numbers.concat(input.nextLine());
        StringCalculator calculator = new StringCalculator();
        try {
            int result = calculator.add(numbers);
            System.out.println("Result: ".concat(String.valueOf(result)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
