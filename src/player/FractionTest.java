package player;

import static org.junit.Assert.*;

import org.junit.Test;

public class FractionTest {

	/**
	 * Testing Strategy: test each method of Fraction (simplify, multiply, divide, convertToDecimal)
	 * For the multiply method, we tested a product of 2 Fractions and a product of a Fraction and 0
	 */
	
	@Test
	public void testAlternateConstructor() {
		Fraction f = new Fraction(3);
		assertEquals(3, f.getNumerator());
		assertEquals(1, f.getDenominator());
	}
	
	@Test
	public void testSimplify() {
		Fraction f = new Fraction(27, 57);
		Fraction s = f.simplify();
		assertEquals(9, s.getNumerator());
		assertEquals(19, s.getDenominator());
	}
	
	@Test
	public void testMultiply() {
		Fraction f1 = new Fraction(6, 8);
		Fraction f2 = new Fraction(7, 3);
		Fraction f = f1.multiply(f2);
		assertEquals(7, f.getNumerator());
		assertEquals(4, f.getDenominator());
	}
	
	@Test
	public void testMultiply0() {
		Fraction f1 = new Fraction(6, 8);
		Fraction f2 = new Fraction(0);
		Fraction f = f1.multiply(f2);
		assertEquals(0, f.getNumerator());
		assertEquals(1, f.getDenominator());
	}
	
	@Test
	public void testDivide() {
		Fraction f1 = new Fraction(1, 4);
		Fraction f2 = new Fraction(1, 16);
		int q = f1.divide(f2);
		assertEquals(4, q);
	}
	
	@Test
	public void testConvert() {
		Fraction f1 = new Fraction(2, 4);
		double dec = f1.convertToDecimal();
		assertTrue(compareDoubles(dec, .5));
	}
	
	/**
	 * Determines whether two decimal values are equal using an epsilon value
	 * @return a boolean that represents whether two double values are equal
	 **/
	public boolean compareDoubles(double a, double b) {
		double epsilon = 0.00000001d;
		return Math.abs(a - b) < epsilon;
	}
	
}
