package org.tddKata;

import java.util.ArrayList;
import java.util.Arrays;

class NegativeNumbersFoundedException extends IllegalArgumentException {
    private final ArrayList<Integer> negatives;

    NegativeNumbersFoundedException(ArrayList<Integer> negatives) {
        this.negatives = negatives;
    }

    @Override
    public String getMessage() {
        ArrayList<String> stringifyNegatives = new ArrayList<>();
        for (Integer val : negatives) {
            stringifyNegatives.add(String.valueOf(val.intValue()));
        }
        return "Negative numbers are prohibited: ".concat(String.join(", ", stringifyNegatives));
    }
}

class DelimitersFormatException extends IllegalArgumentException {
    DelimitersFormatException(String s) {
        super(s);
    }
}

class NumbersInputException extends IllegalArgumentException {
    NumbersInputException(String s) {
        super(s);
    }
}

public class StringCalculator {
    public int add(String numbers) throws NegativeNumbersFoundedException {
        StringCalculatorParser parser = new StringCalculatorParser(numbers);
        parser.parseString();
        Integer[] numbersArray = parser.getNumbers();
        ArrayList<Integer> negatives = new ArrayList<>();
        int sum = 0;
        for (int i = 0; i < numbersArray.length; i++) {
            if (numbersArray[i] >= 1000) {
                continue;
            } else if (numbersArray[i] < 0) {
                negatives.add(numbersArray[i]);
            }
            sum += numbersArray[i];
        }
        if (negatives.size() > 0) {
            throw new NegativeNumbersFoundedException(negatives);
        }
        return sum;
    }
}

class StringCalculatorParser {
    StringCalculatorParser(String stringToParse) {
        this.stringToParse = stringToParse;
    }

    private final String stringToParse;

    private Delimiter[] delimiters;

    private Integer[] numbers;

    public void parseString() throws DelimitersFormatException {
        String delimitersString = "";
        String numbersString = stringToParse;
        if (stringToParse.startsWith("//")) {
            try {
                delimitersString = stringToParse.substring(2, stringToParse.indexOf("\\n"));
            } catch (IndexOutOfBoundsException e) {
                throw new DelimitersFormatException("Unfinished delimiters sequence started with \"//\"");
            }
            numbersString = stringToParse.substring(delimitersString.length() + 4);
        }
        delimiters = parseDelimiters(delimitersString);
        numbers = parseNumbers(numbersString);
    }

    public Integer[] getNumbers() {
        return numbers;
    }

    private Delimiter[] parseDelimiters(String delimitersString) {
        ArrayList<Delimiter> foundDelimiters = new ArrayList<>(Arrays.asList(
                new Delimiter(",", false),
                new Delimiter("\\n", false)
        ));
        if (!isValidBracketsSequence(delimitersString)) {
            if (!delimitersString.isEmpty() && !(delimitersString.equals(",") || delimitersString.equals("\\n"))) {
                foundDelimiters.add(new Delimiter(delimitersString, false));
            }
        } else {
            int count = 0, start = 0;
            char current;
            for (int i = 0; i < delimitersString.length(); i++) {
                current = delimitersString.charAt(i);
                if (current == '[') {
                    if (count == 0) {
                        start = i + 1;
                    }
                    count += 1;
                } else if (current == ']') {
                    count -= 1;
                }
                if (count == 0) {
                    String toAdd = delimitersString.substring(start, i);
                    if (!toAdd.isEmpty()) {
                        if (toAdd.equals(",")) {
                            foundDelimiters.get(0).canRepeat = true;
                        } else if (toAdd.equals("\\n")) {
                            foundDelimiters.get(1).canRepeat = true;
                        } else {
                            foundDelimiters.add(new Delimiter(toAdd, true));
                        }
                    }
                }
            }
        }
        return foundDelimiters.toArray(new Delimiter[0]);
    }

    private boolean isValidBracketsSequence(String delimitersString) {
        if (delimitersString.startsWith("[") && delimitersString.endsWith("]")) {
            int count = 0;
            char current;
            for (int i = 0; i < delimitersString.length(); i++) {
                current = delimitersString.charAt(i);
                if (current == '[') {
                    count += 1;
                } else if (current == ']') {
                    count -= 1;
                }
            }
            return count == 0;
        }
        return false;
    }

    private Integer[] parseNumbers(String numbersString) throws NumbersInputException {
        ArrayList<Integer> foundNumbers = new ArrayList<>(1);
        foundNumbers.add(0);
        if (numbersString.isEmpty()) {
            return foundNumbers.toArray(new Integer[0]);
        }
        Delimiter current;
        int start = 0, i = 0, prevLength = 0;
        while (i < numbersString.length()) {
            current = delimiterOnCurrentPos(numbersString, i);
            if (current != null) {
                if (start == i) {
                    if (i == 0) {
                        throw new NumbersInputException("Delimiter at the start of the string not following a number");
                    } else {
                        throw new NumbersInputException("Delimiter at ".concat(
                                String.valueOf(i - prevLength)).concat(
                                " followed by delimiter at ").concat(String.valueOf(i)
                        ));
                    }
                }
                try {
                    foundNumbers.add(Integer.parseInt(numbersString, start, i, 10));
                } catch (NumberFormatException e) {
                    throw new NumbersInputException("Invalid characters' sequence from ".concat(
                            String.valueOf(start)).concat(" to ").concat(String.valueOf(i)
                    ));
                }
                if (numbersString.charAt(start) == '+') {
                    throw new NumbersInputException("Number written with '+' at ".concat(
                            String.valueOf(start)
                    ));
                }
                i = start = i + current.content.length();
                prevLength = current.content.length();
            } else {
                prevLength = 0;
                i++;
            }
        }
        if (start == i) {
            throw new NumbersInputException("Delimiter at the end of a string not followed by a number");
        }
        try {
            foundNumbers.add(Integer.parseInt(numbersString, start, i, 10));
        } catch (NumberFormatException e) {
            throw new NumbersInputException("Invalid characters' sequence from ".concat(
                    String.valueOf(start)).concat(" to ").concat(String.valueOf(i)
            ));
        }
        if (numbersString.charAt(start) == '+') {
            throw new NumbersInputException("Number written with '+' at ".concat(
                    String.valueOf(start)
            ));
        }
        return foundNumbers.toArray(new Integer[0]);
    }

    private Delimiter delimiterOnCurrentPos(String numbersString, int pos) {
        int resultIndex = -1;
        int[] repeatTimes = new int[delimiters.length];
        Arrays.fill(repeatTimes, 0);
        boolean[] toObserve = new boolean[delimiters.length];
        Arrays.fill(toObserve, true);
        boolean observeFurther = false;
        int[] offset = new int[delimiters.length];
        Arrays.fill(offset, pos);
        for (int i = pos; i < numbersString.length(); i++) {
            for (int j = 0; j < delimiters.length; j++) {
                if (!toObserve[j]) {
                    continue;
                }
                if (delimiters[j].content.charAt(i - offset[j]) != numbersString.charAt(i)) {
                    toObserve[j] = false;
                } else if (i - offset[j] == delimiters[j].content.length() - 1) {
                    toObserve[j] = delimiters[j].canRepeat;
                    resultIndex = j;
                    offset[j] += delimiters[j].content.length();
                    repeatTimes[j]++;
                }
            }
            for (boolean val : toObserve) {
                if (val) {
                    observeFurther = true;
                    break;
                }
            }
            if (!observeFurther) {
                break;
            }
        }
        if (resultIndex == -1) {
            return null;
        }
        if (repeatTimes[resultIndex] > 1) {
            return new Delimiter(delimiters[resultIndex].content.repeat(repeatTimes[resultIndex]), false);
        } else {
            return delimiters[resultIndex];
        }
    }
}

class Delimiter {
    public Delimiter(String delimiter, boolean repeats) {
        content = delimiter;
        canRepeat = repeats;
    }

    String content;

    boolean canRepeat;
}