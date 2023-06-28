package gui;

import fractionexception.MixedFractionException;
import main.MixedFraction;
import jdk.jshell.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Model {

    public String calculateProblem(String problem, char character) throws MixedFractionException {
        String answer;
        JShell shell = JShell.create();

        List<Double> numbers = new ArrayList<>();
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(problem);
        while (matcher.find()) {
            String match = matcher.group();
            double number = Double.parseDouble(match);
            numbers.add(number);
        }

        String formattedProblem = matcher.replaceAll("#");


        for (double number : numbers) {
            formattedProblem = formattedProblem.replaceFirst("#", String.valueOf(number));
        }

        List<SnippetEvent> events = shell.eval(formattedProblem);

        if (!events.isEmpty()) {
            SnippetEvent event = events.get(0);
            if (event.status() == Snippet.Status.VALID) {
                double result = Double.parseDouble(event.value());

                if (event.value().equals("Infinity")) {
                    throw new MixedFractionException("Undefined");
                } else if (Math.floor(result) == result) {
                    int intResult = (int) result;
                    answer = String.valueOf(intResult);
                } else {
                    if (character == 'x') {
                        answer = String.valueOf(result);
                    } else {
                        String resultStr = String.valueOf(result);
                        if (resultStr.length() > 10) {
                            resultStr = String.format("%.3f", result);
                        }
                        answer = resultStr;
                    }
                }
            } else {
                throw new MixedFractionException("Math Error");
            }
        } else {
            throw new MixedFractionException("Math Error");
        }

        return answer;
    }

    public String calculateFraction(String problem) {
        String answer = problem;
        answer = unFormatSuperscript(answer);
        answer = unFormatSubscript(answer);
        Pattern pattern = Pattern.compile("(\\d+)\\s+(\\d+)/(\\d+)|(\\d+)/(\\d+)");
        Matcher matcher = pattern.matcher(answer);
        MixedFraction f1 =  new MixedFraction();
        List<MixedFraction> fractions = new ArrayList<>();
        while (matcher.find()) {
            int wholeNumber = 0;
            int numerator = 0;
            int denominator = 0;
            if (matcher.group(1) != null && matcher.group(2) != null && matcher.group(3) != null) {
                wholeNumber = Integer.parseInt(matcher.group(1).trim());
                numerator = Integer.parseInt(matcher.group(2).trim());
                denominator = Integer.parseInt(matcher.group(3).trim());
            } else if (matcher.group(4) != null && matcher.group(5) != null) {
                numerator = Integer.parseInt(matcher.group(4).trim());
                denominator = Integer.parseInt(matcher.group(5).trim());
            }
            if (numerator > 0 && denominator > 0) {
                MixedFraction mixedFraction = new MixedFraction(wholeNumber, numerator, denominator);
                fractions.add(mixedFraction);
            }
        }

        for (MixedFraction fraction : fractions) {
            double decimal = fraction.toDecimal();
            answer = answer.replace(fraction.toString(), String.valueOf(decimal));
            try {
                f1 = calculate(fractions.get(0), fractions.get(1), '+');
            }
           catch (MixedFractionException e){
              e.getMessage();
           }
            catch (Exception e2){
                e2.getMessage();
            }
        }

        return removeDivision(answer);
    }

    private MixedFraction calculate(MixedFraction operand1, MixedFraction operand2, char operator) throws MixedFractionException {
        return switch (operator) {
            case '+' -> operand1.add(operand2);
            case '-' -> operand1.subtract(operand2);
            case '*' -> operand1.multiplyBy(operand2);
            case '÷' -> operand1.divideBy(operand2);
            default -> throw new MixedFractionException("Invalid operator: " + operator);
        };
    }

    private String unFormatSuperscript(String str) {
        str = str.replaceAll("⁰", "0");
        str = str.replaceAll("¹", "1");
        str = str.replaceAll("²", "2");
        str = str.replaceAll("³", "3");
        str = str.replaceAll("⁴", "4");
        str = str.replaceAll("⁵", "5");
        str = str.replaceAll("⁶", "6");
        str = str.replaceAll("⁷", "7");
        str = str.replaceAll("⁸", "8");
        str = str.replaceAll("⁹", "9");
        str = str.replaceAll("/", "÷");
        str = str.replaceAll("⁄", "/");
        return str;
    }

    private String unFormatSubscript(String str) {
        str = str.replaceAll("₀", "0");
        str = str.replaceAll("₁", "1");
        str = str.replaceAll("₂", "2");
        str = str.replaceAll("₃", "3");
        str = str.replaceAll("₄", "4");
        str = str.replaceAll("₅", "5");
        str = str.replaceAll("₆", "6");
        str = str.replaceAll("₇", "7");
        str = str.replaceAll("₈", "8");
        str = str.replaceAll("₉", "9");
        return str;
    }

    private String removeDivision(String str) {
        str = str.replaceAll("÷", "/");
        return str;
    }

    public String decimalToMixedFraction(double decimal) {
        int whole = (int) decimal;
        double fractionalPart = decimal - whole;
        int numerator = 0;
        int denominator = 1;
        boolean isRepeating = false;

        for (int i = 1; i <= 12; i++) {
            double multiplied = fractionalPart * Math.pow(10, i);
            int digit = (int) Math.floor(multiplied) % 10;
            if (digit == 0 && i > 1) {
                numerator = (int) (fractionalPart * Math.pow(10, i));
                denominator = (int) Math.pow(10, i);
                break;
            }
            if (i == 12) {
                isRepeating = true;
                break;
            }
        }

        if (isRepeating) {
            StringBuilder result = new StringBuilder();
            result.append(whole).append(".");
            List<Integer> pattern = new ArrayList<>();
            Map<Integer, Integer> seen = new HashMap<>();
            int currentIndex = 0;

            while (!seen.containsKey(currentIndex)) {
                int currentDigit = (int) Math.floor(fractionalPart * 10);
                pattern.add(currentDigit);
                seen.put(currentIndex, currentDigit);
                fractionalPart = fractionalPart * 10 - currentDigit;
                currentIndex++;
                if (fractionalPart == 0) {
                    break;
                }
            }

            if (fractionalPart != 0) {
                int repeatIndex = seen.get(currentIndex);
                pattern.add(currentIndex, repeatIndex);
            }

            for (int i = 0; i < pattern.size(); i++) {
                if (i == currentIndex) {
                    result.append("(");
                }
                result.append(pattern.get(i));
                if (i == pattern.size() - 1) {
                    result.append(")");
                }
            }

            return String.format("%.3f", Double.parseDouble(result.toString().replace(")", "")));
        } else if (numerator == 0) {
            return Integer.toString(whole);
        } else {
            int gcd = findGCD(numerator, denominator);
            numerator /= gcd;
            denominator /= gcd;
            if (whole == 0) {
                return toFormat(numerator, denominator);
            } else {
                return whole + toFormat(numerator, denominator);
            }
        }
    }

    private int findGCD(int a, int b) {
        if (b == 0) {
            return a;
        } else {
            return findGCD(b, a % b);
        }

    }

    public String toFormat(int numerator, int denominator){
        View view = new View();
        return view.diagonalFraction(numerator, denominator);
    }
}