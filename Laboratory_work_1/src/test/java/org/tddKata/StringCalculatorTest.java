package org.tddKata;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class StringCalculatorTest {
    StringCalculator testCalculator = new StringCalculator();

    @Test
    public void shouldReturnZero() {
        try {
            org.junit.jupiter.api.Assertions.assertEquals(testCalculator.add(""), 0);
        } catch (NegativeNumbersFoundedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void shouldAcceptOneNumber() {
        try {
            org.junit.jupiter.api.Assertions.assertEquals(testCalculator.add("1"), 1);
        } catch (NegativeNumbersFoundedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void shouldAcceptTwoNumbers() {
        try {
            org.junit.jupiter.api.Assertions.assertEquals(testCalculator.add("1,2"), 3);
        } catch (NegativeNumbersFoundedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void shouldAcceptMoreNumbers() {
        try {
            org.junit.jupiter.api.Assertions.assertEquals(testCalculator.add("1,2,3,4,5,6,7,8,9,10,0"), 55);
        } catch (NegativeNumbersFoundedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void shouldFailOnRedundantComma() {
        org.junit.Assert.assertThrows(NumbersInputException.class, () -> testCalculator.add("0,,123"));
    }

    @Test
    public void shouldPassWithNewline() {
        org.junit.jupiter.api.Assertions.assertEquals(testCalculator.add("450\\n450"), 900);
    }

    @Test
    public void shouldPassWithNewlineAndComma() {
        try {
            org.junit.jupiter.api.Assertions.assertEquals(testCalculator.add("54\\n23,0\\n81"), 158);
        } catch (NegativeNumbersFoundedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void shouldFailOnRedundantNewline() {
        org.junit.Assert.assertThrows(NumbersInputException.class, () -> testCalculator.add("\\n90"));
    }

    @Test
    public void shouldAcceptOneDelimiter() {
        try {
            org.junit.jupiter.api.Assertions.assertEquals(testCalculator.add("//;=-=;\\n70\\n80,90;=-=;100"), 340);
        } catch (NegativeNumbersFoundedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void shouldFailOnUnrepeatableDelimiter() {
        org.junit.Assert.assertThrows(NumbersInputException.class, () -> testCalculator.add("//%^\\n9%^%^7"));
    }

    @Test
    public void shouldFailOnInvalidStringStructure() {
        org.junit.Assert.assertThrows(DelimitersFormatException.class, () -> testCalculator.add("//80,34"));
    }

    @Test
    public void shouldAcceptMinusAsDelimiter() {
        try {
            org.junit.jupiter.api.Assertions.assertEquals(testCalculator.add("//-\\n0-56-11-23"), 90);
        } catch (NegativeNumbersFoundedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void shouldFailOnNegativeNumbers() {
        org.junit.Assert.assertThrows(NegativeNumbersFoundedException.class, () -> testCalculator.add("124,-90,-3"));
    }

    @Test
    public void shouldReturnNegativeNumbersList() {
        String[] negatives = new String[]{"-5", "-19", "-74"};
        try {
            testCalculator.add("-5,-19\\n90,-74");
        } catch (NegativeNumbersFoundedException e) {
            String answer = "Negative numbers are prohibited: ".concat(
                    String.join(", ", Arrays.asList(negatives)
                    ));
            org.junit.jupiter.api.Assertions.assertEquals(answer, e.getMessage());
        }
    }

    @Test
    public void shouldIgnoreMoreThanThousand() {
        org.junit.jupiter.api.Assertions.assertEquals(testCalculator.add("//0\\n11110222203333015"), 15);
    }

    @Test
    public void shouldAcceptVariableDelimiter() {
        org.junit.jupiter.api.Assertions.assertEquals(testCalculator.add("//[*$]\\n100*$100*$*$*$100*$*$*$*$*$*$5"), 305);
    }

    @Test
    public void shouldAcceptTwoDelimiters() {
        org.junit.jupiter.api.Assertions.assertEquals(testCalculator.add("//[??][???]\\n5??5???7????21??????8??????10"), 56);
    }

    @Test
    public void shouldAcceptMoreDelimiters() {
        org.junit.jupiter.api.Assertions.assertEquals(testCalculator.add("//[#][!][=][:]\\n1001#3!!!4=====8:2,7\\n9"), 33);

    }

    @Test
    public void shouldFailOnRedundantDelimiter() {
        org.junit.jupiter.api.Assertions.assertThrows(NumbersInputException.class,
                () -> testCalculator.add("//[&][{][%][^]\\n43^90%%34&1{{")
        );
    }

    @Test
    public void shouldAcceptMoreWideDelimiters() {
        org.junit.jupiter.api.Assertions.assertEquals(testCalculator.add("//[math][no]\\n9nono7mathmathmath3"), 19);
    }

    @Test
    public void shouldFailOnMismatchingDelimiters() {
        org.junit.jupiter.api.Assertions.assertThrows(NumbersInputException.class,
                () -> testCalculator.add("//[</>][<\\>]\\n78/\\22"));
    }
}