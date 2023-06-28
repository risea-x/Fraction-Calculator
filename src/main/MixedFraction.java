package main;

/**
 * The class Mixed Fraction represents a mixed fraction.
 * @author Neil Briones
 * @author Clarence Cayton
 * @author Junsel Fabe
 * @author Dylan Magpili
 * @author Hans Viduya
 * @version JDK 19
 */

public class MixedFraction extends Fraction {

    private int wholeNumber;

    /**
     * Mixed Fraction default constructor.
     */
    public MixedFraction(){
        this(0,0,1);
    }

    /**
     * Mixed Fraction constructor with two parameters.
     * @param wholeNumber the whole number of the mixed fraction
     * @param fraction the fraction of the Fraction class
     */
    public MixedFraction(int wholeNumber, Fraction fraction){
        this(wholeNumber, fraction.getNumerator(), fraction.getDenominator());
    }

    /**
     * Mixed Fraction constructor with all fields as parameters.
     * @param wholeNumber the whole number of the mixed fraction
     * @param numerator the numerator of the Fraction class
     * @param denominator the denominator of the Fraction class
     */
    public MixedFraction(int wholeNumber, int numerator, int denominator){
        super(numerator, denominator);
        this.wholeNumber = wholeNumber;
    }

    /**
     * Mixed Fraction constructor with one parameter.
     * @param fraction the fraction of the Fraction class
     */
    public MixedFraction(Fraction fraction){
        this(0, fraction.getNumerator(), fraction.getDenominator());
    }

    /**
     * Whole number setter method.
     * @param wholeNumber set the whole number of the Mixed Fraction class
     */
    public void setWholeNumber(int wholeNumber) {
        this.wholeNumber = wholeNumber;
    }

    /**
     * Gets the whole number of the mixed fraction.
     * @return this mixed fraction's whole number
     */
    public int getWholeNumber() {
        return wholeNumber;
    }

    /**
     * Gets the fraction part of the mixed fraction.
     * @return this fraction's numerator and denominator
     */
    public Fraction getFractionPart(){
        return new Fraction(getNumerator(), getDenominator());
    }

    /**
     * Make the mixed fraction as an improper fraction.
     * @return this fraction's numerator and denominator
     */
    public Fraction toFraction(){
        int numerator = Math.abs(wholeNumber) * getDenominator() + getNumerator();
        if (wholeNumber < 0) {
            numerator = -numerator;
        }
        return new Fraction(numerator, getDenominator());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double toDecimal() {
        return wholeNumber + super.toDecimal();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fraction simplify() {
        int sign;
        if (getNumerator() < 0) {
            sign = -1;
        } else {
            sign = 1;
        }

        int numerator = Math.abs(getNumerator()) + Math.abs(wholeNumber) * getDenominator();
        int gcd = gcd(numerator, getDenominator());
        numerator /= gcd;
        int denominator = getDenominator() / gcd;

        if (sign < 0) {
            wholeNumber = -numerator / denominator;
            numerator = -numerator % denominator;
        } else {
            wholeNumber = numerator / denominator;
            numerator = numerator % denominator;
        }

        setNumerator(numerator);
        setDenominator(denominator);
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (this.getNumerator() == 0) {
            return Integer.toString(wholeNumber);
        } else if (this.wholeNumber == 0) {
            return super.toString();
        } else {
            return this.wholeNumber + " " + Math.abs(getNumerator()) + "/" + getDenominator();
        }
    }

    /**
     * Addition of two mixed fractions.
     * @param other the other mixed fraction that will be used for addition
     * @return a new mixed fraction
     */
    public MixedFraction add(MixedFraction other) {
        Fraction thisFraction = this.toFraction();
        Fraction otherFraction = other.toFraction();
        Fraction resultFraction = thisFraction.add(otherFraction);
        return new MixedFraction(resultFraction);
    }

    /**
     * Subtraction of two mixed fraction.
     * @param other the other mixed fraction that will be used for subtraction
     * @return a new mixed fraction
     */
    public MixedFraction subtract(MixedFraction other) {
        Fraction thisFraction = this.toFraction();
        Fraction otherFraction = other.toFraction();
        Fraction resultFraction = thisFraction.subtract(otherFraction);
        return new MixedFraction(resultFraction);
    }

    /**
     * Multiplication of two mixed fraction.
     * @param other the other mixed fraction that will be used for multiplication
     * @return a new mixed fraction
     */
    public MixedFraction multiplyBy(MixedFraction other) {
        Fraction thisFraction = this.toFraction();
        Fraction otherFraction = other.toFraction();
        Fraction resultFraction = thisFraction.multiply(otherFraction);
        return new MixedFraction(resultFraction);
    }

    /**
     * Division of two mixed fraction.
     * @param other the other mixed fraction that will be used for division
     * @return a new mixed fraction
     */
    public MixedFraction divideBy(MixedFraction other) {
        Fraction thisFraction = this.toFraction();
        Fraction otherFraction = other.toFraction();
        Fraction resultFraction = thisFraction.divide(otherFraction).simplify();
        return new MixedFraction(resultFraction);
    }
}

