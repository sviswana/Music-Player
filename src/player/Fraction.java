package player;

import java.math.BigInteger;

/**
 * An immutable datatype object that represents a Fraction given a numerator and denominator.
 * Will be used to represent values for meters and note lengths. This class will also 
 * help determine tick counts.
 */

public class Fraction {
    
    private final int numerator;
    private final int denominator;
    
    /**
     * Initializes the numerator and denominator of a Fraction
     * 
     * @param int numerator, int denominator 
     */
    public Fraction(int numerator, int denominator)
    {
        if (denominator <=0) {
            throw new IllegalArgumentException("Denominator cannot be negative or zero");
        }
        this.numerator = numerator;
        this.denominator = denominator;
    }
    
    /**
     * Initializes the numerator of a Fraction and sets the denominator to 1
     * 
     * @param int numerator
     */
    public Fraction(int numerator) {
        this(numerator, 1);
    }
    
    /**
     * Gets the numerator of a Fraction
     * 
     * @return the numerator of a Fraction object
     **/
    public int getNumerator() {
        return this.numerator;
    }

    /**
     * Gets the denominator of a Fraction
     * 
     * @return the denominator of a Fraction object
     **/
    public int getDenominator() {
        return this.denominator;
    }
    
    /**
     * Multiplies two fractions together
     * 
     * @return a new Fraction object that represents the product of two Fractions
     **/
    public Fraction multiply(Fraction other){
    	int numerator = this.numerator * other.numerator;
    	int denominator = this.denominator * other.denominator;
    	
    	Fraction mult = new Fraction(numerator, denominator);
    	return mult.simplify();	
    }
    
    /**
     * Divides one Fraction by another Fraction. This method is used to calculate a modified tempo
     * depending on the note length and original tempo, so all quotients should be integers
     * 
     * @return an integer that represents the quotient of two Fractions
     **/
    public int divide(Fraction other){
    	// calculate the quotient by multiplying by the reciprocal of other
    	int numerator = this.numerator * other.denominator;
    	int denominator = this.denominator * other.numerator;
    	
    	Fraction div = new Fraction(numerator, denominator);
    	return div.simplify().getNumerator();
    }
    
    /**
     * Simplifies a Fraction to lowest terms
     * 
     * @return a new Fraction object that represents the same Fraction in lowest terms
     **/
    public Fraction simplify(){
        BigInteger num = new BigInteger(""+numerator);
        BigInteger denom = new BigInteger(""+denominator);
        
        // calculate the gcd of the numerator and denominator to reduce the Fraction
        BigInteger gcd = num.gcd(denom);
        
        int newNum = num.intValue()/gcd.intValue();
        int newDenom = denom.intValue()/gcd.intValue();
        
        return new Fraction(newNum, newDenom);
    }
    
    /**
     * Converts a Fraction to a decimal value
     * 
     * @return a decimal representation of a Fraction
     **/
    public double convertToDecimal(){
    	return ((double) this.numerator/this.denominator);
    }
    
    /**
     * Returns a Fraction in an easy-to-read format
     * 
     * @return a String representation of a Fraction object
     */
    @Override
    public String toString() {
        return Integer.toString(numerator) + '/' + Integer.toString(denominator); 
    }
}
