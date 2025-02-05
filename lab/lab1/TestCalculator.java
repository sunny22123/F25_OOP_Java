package lab1;

import static org.junit.Assert.*;

import org.junit.Test;


public class TestCalculator {
	
	public Calculator c;
	
	@Test
	public void test1_CalcSinglePlusOperator() {
		c = new Calculator();
		c.expression = "3 + 5";
		c.calculate();
		assertEquals(8, c.result, 0);
	}
	
	@Test
	public void test2_CalcSingleMinusOperator() {
		c = new Calculator();
		c.expression = "4 - 10";
		c.calculate();
		assertEquals(-6.0, c.result, 0);
	}
	
	@Test
	public void test3_CalcSingleMultiplyOperator() {
		c = new Calculator();
		c.expression = "3 * 5";
		c.calculate();
		assertEquals(15, c.result, 0);
	}
	
	@Test
	public void test4_CalcSingleDivideOperator() {
		c = new Calculator();
		c.expression = "9 / 3";
		c.calculate();
		assertEquals(3.0, c.result, 0);
	}
	
		
	@Test
	public void test5_CalcNegativeOperand() {
		c = new Calculator();
		c.expression = "-10 + 6";
		c.calculate();
		assertEquals(-4, c.result, 0);
	}
	

	@Test
	public void test6_CalcMultiOperator() {
		c = new Calculator();
		c.expression = "3 + 5 - 1";
		c.calculate();
		assertEquals(7, c.result, 0);
	}
	
	@Test
	public void test7_CalcDivideByZero() {
		c = new Calculator();
		c.expression = "1 / 0";
		c.calculate();
		assertEquals(Double.POSITIVE_INFINITY, c.result, 0);
	}
	
	@Test
	public void test8_CalcEmptyExpression() {
		c = new Calculator();
		c.expression = "";
		c.calculate();
		assertEquals(0, c.result, 0);
	}
	
	@Test
	public void test9_SingleOperand() {
		c = new Calculator();
		c.expression = "3";
		c.calculate();
		assertEquals(3, c.result, 0);
	}
		
}
