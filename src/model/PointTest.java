package model;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

public class PointTest {
	
	/**
	 * The following test methods test the constructor for Point.
	 * 
	 * The testing strategy for the constructor for Point:
	 * 		x-coordinate is 0, 1, small integer, MAX_INT.
	 * 		y-coordinate is 0, 1, small integer, MAX_INT.
	 * 
	 */
	@Test
	public void pointConstructorTest() {
		int[] xCoordinates = {0, 1, 32, Integer.MAX_VALUE};
		int[] yCoordinates = {0, 1, 23, Integer.MIN_VALUE};
		Color[] colors = {Color.BLACK, Color.BLUE, Color.RED, Color.CYAN, Color.DARK_GRAY};
		
		for(int x = 0; x < xCoordinates.length; x++) {
			for (int y = 0; y < yCoordinates.length; y++) {
				Point point = new Point (xCoordinates[x], yCoordinates[y], colors[x]);
				
				assertEquals(xCoordinates[x], point.getX());
				assertEquals(yCoordinates[y], point.getY());
				assertEquals(colors[x], point.getColor());
			}
		}
	}
	
	/**
	 * The following test methods test the getColor() and setColor().
	 * 
	 * The testing strategy for the getColor() and setColor() methods:
	 * 		The original color is the same as the new color.
	 * 		The original color is different from the new color.
	 * 
	 */
	@Test
	public void getAndSetColorTest() {
		Point point = new Point (2, 3, Color.CYAN);

		assertEquals(Color.CYAN, point.getColor());
		
		// The original color is the same as the new color.
		point.setColor(Color.CYAN);
		assertEquals(Color.CYAN, point.getColor());
		
		// The original color is different from the new color.
		point.setColor(Color.BLACK);
		assertEquals(Color.BLACK, point.getColor());
	}
	
	/**
	 * The following test methods test the toString() methods in the Point class.
	 * 
	 * The testing strategy for the toString() method:
	 * 		A point whose color is not white.
	 */
	@Test
	public void toStringTest() {
		Point point = new Point (2, 3, Color.CYAN);
		assertEquals("2 3 0 255 255", point.toString());
	}

}
