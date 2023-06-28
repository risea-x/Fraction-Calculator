package main;

/**
 * The class Fraction represents a fraction.
 * @author Neil Briones
 * @author Clarence Cayton
 * @author Junsel Fabe
 * @author Dylan Magpili
 * @author Hans Viduya
 * @version JDK 19
 */

public class Fraction{
    private int numerator;
    private int denominator;

    /**
     * Fraction default constructor.
     */
    public Fraction(){
        numerator = 0;
        denominator = 1;
    }

    /**
     * Fraction constructor with two parameters.
     * @param numerator the numerator of the Fraction class
     * @param denominator the denominator of the Fraction class
     */
    public Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     * Numerator setter method.
     * @param numerator set the numerator of the Fraction class
     */
    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    /**
     * Denominator setter method.
     * @param denominator set the denominator of the Fraction class
     */
    public void setDenominator(int denominator) {
        this.denominator = denominator;
    }

    /**
     * Gets the numerator of the fraction.
     * @return this fraction's numerator
     */
    public int getNumerator() {
        return numerator;
    }

    /**
     * Gets the numerator of the fraction.
     * @return this fraction's denominator
     */
    public int getDenominator() {
        return denominator;
    }

    /**
     * Addition of two fractions.
     * @param other the other fraction that will be used for addition
     * @return a new fraction
     */
    public Fraction add(Fraction other){
        int newNumerator = this.numerator * other.denominator + this.denominator * other.numerator;
        int newDenominator = this.denominator * other.denominator;
        return new Fraction(newNumerator, newDenominator);
    }

    /**
     * Subtraction of two fraction.
     * @param other the other fraction that will be used for subtraction
     * @return a new fraction
     */
    public Fraction subtract(Fraction other) {
        int newNumerator = this.numerator * other.denominator - this.denominator * other.numerator;
        int newDenominator = this.denominator * other.denominator;
        return new Fraction(newNumerator, newDenominator);
    }

    /**
     * Multiplication of two fraction.
     * @param other the other fraction that will be used for multiplication
     * @return a new fraction
     */
    public Fraction multiply(Fraction other) {
        int newNumerator = this.numerator * other.numerator;
        int newDenominator = this.denominator * other.denominator;
        return new Fraction(newNumerator, newDenominator);
    }

    /**
     * Division of two fraction.
     * @param other the other fraction that will be used for division
     * @return a new fraction
     */
    public Fraction divide(Fraction other) {
        int newNumerator = this.numerator * other.denominator;
        int newDenominator = this.denominator * other.numerator;
        return new Fraction(newNumerator, newDenominator);
    }

    /**
     * Simplification of a fraction.
     * @return a new fraction
     */
    public Fraction simplify() {
        int gcd = gcd(this.numerator, this.denominator);
        return new Fraction(this.numerator / gcd, this.denominator / gcd);
    }

    /**
     * Converts the fraction to a readable format.
     * @return an easier way to read the object class
     */
    public String toString() {
        if (denominator == 1){
            return String.valueOf(numerator);
        }
        return numerator + "/" + denominator;
    }

    /**
     * Make the fraction a decimal number.
     * @return a type of double number
     */
    public double toDecimal(){
        return (double) numerator/denominator;
    }

    /**
     * Converts improper fraction to a mixed fraction.
     * @deprecated already replaced by a new method {@link MixedFraction#simplify() Fraction}
     */
    @Deprecated
    public String toMixedNumber() {
        int wholeNumber = numerator / denominator;
        int newNumerator = numerator % denominator;
        if (wholeNumber == 0) {
            return newNumerator + "/" + denominator;
        } else if (newNumerator == 0) {
            return Integer.toString(wholeNumber);
        } else {
            return wholeNumber + " " + newNumerator + "/" + denominator;
        }
    }

    /**
     * Find the GCD of a fraction.
     * @param firstNumber first number to find the gcd
     * @param secondNumber second number to find the gcd
     * @return a GCD of both numbers
     */
    public int gcd(int firstNumber, int secondNumber) {
        if (secondNumber == 0) {
            return firstNumber;
        }
        return gcd(secondNumber, firstNumber % secondNumber);
    }

}