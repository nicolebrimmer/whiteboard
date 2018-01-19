package model;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;

import org.junit.Test;

public class WhiteboardTest {
	private final static Color originalColor = Color.WHITE;
	
	/**
	 * The following test methods test the Whiteboard constructor
	 * and get methods.
	 * 
	 * The testing strategy for the Whiteboard constructor and the get
	 * methods:
	 * 		Create a whiteboard
	 * 			Make sure that each Point object in the Whiteboard object
	 * 			has the default color of white.
	 * 		Confirm the values of the getName(), getHeight() and getWidth()
	 * 		methods.
	 * 		
	 */
	
	// Create a whiteboard
	@Test
	public void whiteboardConstructorTest() {
		int width = 3, height = 4;
		
		Whiteboard whiteboard = new Whiteboard("first whiteboard", width, height);
		Color[][] colors = createColorArray(width, height, originalColor);
		
		confirmColors(whiteboard, colors);
	}
	
	// Confirm the value of the getName() method.
	@Test
	public void getNameTest() {
		String name = "first whiteboard";
		Whiteboard whiteboard = new Whiteboard(name, 3, 4);
		assertEquals(name, whiteboard.getName());
	}
	
	// Confirm the value of the getHeight() method.
	@Test
	public void getHeightTest() {
		int width = 3, height = 4;
		Whiteboard whiteboard = new Whiteboard("first whiteboard", width, height);
		assertEquals(height, whiteboard.getHeight());
	}
	
	// Confirm the value of the getWidth() method.
	@Test
	public void getWidthTest() {
		int width = 3, height = 4;
		Whiteboard whiteboard = new Whiteboard("first whiteboard", width, height);
		assertEquals(width, whiteboard.getWidth());
	}
	
	/**
	 * The following test methods test the Whiteboard setColor() and getColor()
	 * methods.
	 * 
	 * The testing strategy for the Whiteboard setColor() and getColor() methods:
	 * 		Change a colors of a few of the pixels in the Whiteboard object
	 * 		and confirm that they have indeed been changed.
	 * 		
	 */
	
	@Test
	public void changeColorsTest() {
		int width = 3, height = 4;
		
		Whiteboard whiteboard = new Whiteboard("first whiteboard", width, height);
		Color[][] colors = createColorArray(width, height, originalColor);
		
		confirmColors(whiteboard, colors);
		
		whiteboard.setColor(2, 2, Color.RED);
		colors[2][2] = Color.RED;
		confirmColors(whiteboard, colors);
		
		whiteboard.setColor(1, 3, Color.YELLOW);
		colors[3][1] = Color.YELLOW;
		confirmColors(whiteboard, colors);
	}
	
	/**
	 * The following test methods test the colorPoints method in the Whiteboard class.
	 * 
	 * The testing strategy for the colorPoints method:
	 * 		pointsOnThickLine 
	 * 			is empty
	 * 			contains one Point object
	 * 			contains more than one Point object
	 * 
	 * Note that these tests could be further automated.  However, automation of the following
	 * test methods would result in helper methods that would strongly resemble the colorPoints method
	 * itself.  Therefore, this automation has not been performed.
	 * 
	 */
	
	// pointsOnThickLine is empty
	@Test
	public void colorPointsEmptyTest() {
		int width = 3, height = 4;
		
		ArrayList<Point> pointsToBeColored = new ArrayList<Point>();
		Color newColor = Color.BLUE;
		
		Whiteboard whiteboard = new Whiteboard("first whiteboard", width, height);
		Color[][] colors = createColorArray(width, height, originalColor);
		
		whiteboard.colorPoints(pointsToBeColored, newColor);
		
		confirmColors(whiteboard, colors);
	}
	
	// pointsOnThickLine contains one Point object
	@Test
	public void colorPointsOnePointTest() {
		int width = 3, height = 4;
		
		ArrayList<Point> pointsToBeColored = new ArrayList<Point>();
		Color newColor = Color.BLUE;
		pointsToBeColored.add(new Point (2, 2, newColor));
		
		Whiteboard whiteboard = new Whiteboard("first whiteboard", width, height);
		Color[][] colors = createColorArray(width, height, originalColor);
		colors[2][2] = newColor;
		
		whiteboard.colorPoints(pointsToBeColored, newColor);
		
		confirmColors(whiteboard, colors);
	}
	
	// pointsOnThickLine contains three Point objects
	@Test
	public void colorPointsThreePointTest() {
		int width = 3, height = 4;
		
		ArrayList<Point> pointsToBeColored = new ArrayList<Point>();
		Color newColor = Color.BLUE;
		pointsToBeColored.add(new Point (2, 2, newColor));
		pointsToBeColored.add(new Point (2, 3, newColor));
		pointsToBeColored.add(new Point (2, 0, newColor));
		
		Whiteboard whiteboard = new Whiteboard("first whiteboard", width, height);
		Color[][] colors = createColorArray(width, height, originalColor);
		colors[2][2] = newColor;
		colors[3][2] = newColor;
		colors[0][2] = newColor;
		
		whiteboard.colorPoints(pointsToBeColored, newColor);
		
		confirmColors(whiteboard, colors);
	}
	
	/**
	 * The following test methods test the getPointsOnLine method in the Whiteboard class.
	 * 
	 * The testing strategy for the getPointsOnLine method:
	 * 		A vertical line
	 * 		A horizontal line
	 * 		A diagonal line (whose slope is not 1)
	 * 
	 */
	
	// A vertical line
	@Test
	public void getPointsOnVerticalLineTest() {
		Color colorOfLine = Color.RED;
		ArrayList<Point> actualPointsOnLine = Whiteboard.getPointsOnLine(colorOfLine, 0, 0, 0, 3);
		Point[] expectedPointsOnLine = {new Point(0, 0, colorOfLine), 
										new Point(0, 1, colorOfLine),
										new Point(0, 2, colorOfLine),
										new Point(0, 3, colorOfLine)};
		
		equalArraysOfPoints(expectedPointsOnLine, actualPointsOnLine);
	}
	
	// A horizontal line
	@Test
	public void getPointsOnHorizontalLineTest() {
		Color colorOfLine = Color.RED;
		ArrayList<Point> actualPointsOnLine = Whiteboard.getPointsOnLine(colorOfLine, 0, 0, 3, 0);
		Point[] expectedPointsOnLine = {new Point(0, 0, colorOfLine), 
										new Point(1, 0, colorOfLine),
										new Point(2, 0, colorOfLine),
										new Point(3, 0, colorOfLine)};
		
		equalArraysOfPoints(expectedPointsOnLine, actualPointsOnLine);
	}
	
	// A diagonal line (whose slope is not 1)
	@Test
	public void getPointsOnDiagonalLineTest() {
		Color colorOfLine = Color.RED;
		ArrayList<Point> actualPointsOnLine = Whiteboard.getPointsOnLine(colorOfLine, 0, 3, 4, 5);
		Point[] expectedPointsOnLine = {new Point(0, 3, colorOfLine), 
										new Point(1, 3, colorOfLine),
										new Point(2, 4, colorOfLine),
										new Point(3, 4, colorOfLine),
										new Point(4, 5, colorOfLine)};
		
		equalArraysOfPoints(expectedPointsOnLine, actualPointsOnLine);
	}
	
	/**
	 * The following test methods test the addLine method in the Whiteboard class.
	 * 
	 * The testing strategy for the addLine method:
	 * 		A thickness of 1
	 * 		A thickness of 3
	 * 		A diagonal line whose thickness comes into contact with the boundary
	 * 
	 */
	
	// Add a line with a thickness of one
	@Test
	public void addLineThicknessOfOne() {
		int width = 6, height = 6;
		Color newColor = Color.BLUE;
		
		Whiteboard whiteboard = new Whiteboard("first whiteboard", width, height);
		whiteboard.addLine(newColor, 0, 3, 4, 5, 1);
		
		Color[][] colors = createColorArray(width, height, originalColor);
		colors[3][0] = newColor;
		colors[3][1] = newColor;
		colors[4][2] = newColor;
		colors[4][3] = newColor;
		colors[5][4] = newColor;
		
		confirmColors(whiteboard, colors);
	}
	
	// Add a diagonal line with a thickness of three whose thickness comes into contact with the boundary
	@Test
	public void addLineThicknessOfThree() {
		int width = 6, height = 6;
		Color newColor = Color.BLUE;
		
		Whiteboard whiteboard = new Whiteboard("first whiteboard", width, height);
		whiteboard.addLine(newColor, 0, 3, 4, 5, 3);
		
		Color[][] colors = createColorArray(width, height, originalColor);
		colors[2][0] = newColor;
		colors[3][0] = newColor;
		colors[4][0] = newColor;
		colors[2][1] = newColor;
		colors[3][1] = newColor;
		colors[4][1] = newColor;
		colors[5][1] = newColor;
		colors[2][2] = newColor;
		colors[3][2] = newColor;
		colors[4][2] = newColor;
		colors[5][2] = newColor;
		colors[3][3] = newColor;
		colors[4][3] = newColor;
		colors[5][3] = newColor;
		colors[3][4] = newColor;
		colors[4][4] = newColor;
		colors[5][4] = newColor;
		colors[4][5] = newColor;
		colors[5][5] = newColor;
		
		confirmColors(whiteboard, colors);
	}
	
	// Add a line that is moving to the left horizontally
	// This is a regression test
	@Test
	public void addLineMovingToLeftHorizontally() {
		int width = 6, height = 6;
		Color newColor = Color.BLUE;
		
		Whiteboard whiteboard = new Whiteboard("first whiteboard", width, height);
		whiteboard.addLine(newColor, 4, 5, 0, 3, 1);
		
		
		Color[][] colors = createColorArray(width, height, originalColor);
		colors[3][0] = newColor;
		colors[3][1] = newColor;
		colors[4][2] = newColor;
		colors[4][3] = newColor;
		colors[5][4] = newColor;
		
		confirmColors(whiteboard, colors);
	}
	
	// Add a line that is moving down
	// This is a regression test
	@Test
	public void addLineMovingDown() {
		int width = 6, height = 6;
		Color newColor = Color.BLUE;
		
		Whiteboard whiteboard = new Whiteboard("first whiteboard", width, height);
		whiteboard.addLine(newColor, 4, 4, 4, 1, 1);
		
		Color[][] colors = createColorArray(width, height, originalColor);
		colors[4][4] = newColor;
		colors[3][4] = newColor;
		colors[2][4] = newColor;
		colors[1][4] = newColor;
		
		confirmColors(whiteboard, colors);
	}
	
	// Add a very steep line
	// This is a regression test
	@Test
	public void addSteepLine() {
		int width = 6, height = 6;
		Color newColor = Color.BLUE;
		
		Whiteboard whiteboard = new Whiteboard("first whiteboard", width, height);
		whiteboard.addLine(newColor, 1, 4, 0, 0, 1);
		
		Color[][] colors = createColorArray(width, height, originalColor);
		colors[0][0] = newColor;
		colors[1][0] = newColor;
		colors[2][1] = newColor;
		colors[3][1] = newColor;
		colors[4][1] = newColor;
		
		confirmColors(whiteboard, colors);
	}
	
	/**
	 * The following test methods test the toString method in the Whiteboard class
	 * 
	 * The testing strategy for the toSting method:
	 * 		The whiteboard contains both points that have been changed and points that
	 * 		have not been changed.
	 * 		
	 */
	@Test
	public void toStringTest() {
		int width = 2, height = 2;
		Color newColor = Color.BLUE;
		
		Whiteboard whiteboard = new Whiteboard("first whiteboard", width, height);
		whiteboard.addLine(newColor, 0, 0, 1, 1, 1);
		
		String expectedString = "0 0 0 0 255 "
							   +"1 1 0 0 255 ";
		
		assertEquals(expectedString, whiteboard.toString());
	}
	
	/**
	 * The following test methods test the addUsername, removeUsername and getUsernames
	 * methods in the Whiteboard class.
	 * 
	 * The testing strategy for the addUsername, removeUsername, containsUsername and getUsername methods:
	 * 		add a few usernames
	 * 		remove a few usernames
	 * 
	 */
	
	@Test
	public void usernamesTest() {
		int width = 5, height = 5;
		Whiteboard whiteboard = new Whiteboard("first whiteboard", width, height);
		
		whiteboard.addUsername("Barry");
		assertEquals("Barry", whiteboard.getUsernames());
		assertTrue(whiteboard.hasUsername("Barry"));
		assertFalse(whiteboard.hasUsername("Mary"));
		
		whiteboard.addUsername("Mary");
		assertEquals("Barry Mary", whiteboard.getUsernames());
		
		whiteboard.addUsername("Hello");
		assertEquals("Barry Mary Hello", whiteboard.getUsernames());
		
		whiteboard.removeUsername("Mary");
		assertEquals("Barry Hello", whiteboard.getUsernames());
	}
	
	/**
	 * The following test methods test the clearWhiteboard method in the Whiteboard class.
	 * 
	 * The test strategy for the clearWhiteboard method:
	 * 		Change some colors in the whiteboard and then reset the whiteboard.
	 * 
	 */
	@Test
	public void clearWhiteboardTest() {
		int width = 6, height = 7;
		Color newColor = Color.BLUE;
		
		Whiteboard whiteboard = new Whiteboard("first whiteboard", width, height);
		whiteboard.addLine(newColor, 0, 0, 4, 3, 2);
		whiteboard.clearWhiteboard();
		
		Color[][] expectedColors = createColorArray(width, height, Color.WHITE);
		confirmColors(whiteboard, expectedColors);
	}
	
	/**
	 * Asserts that two Array Lists of Point objects are equal.
	 * 
	 * @param expected
	 * @param actual
	 * @throws an AssertionError if list1 does not equal list2;
	 * 		   in order for the two lists to be equal, they must
	 * 		   contain the equal Point objects (objects whose fields
	 * 		   are identical) in the same order
	 */
	private void equalArraysOfPoints(Point[] expected, ArrayList<Point> actual) {
		assertEquals(expected.length, actual.size());
		
		for (int i = 0; i < expected.length; i++) {
			Point expectedPoint = expected[i];
			Point actualPoint = actual.get(i);
			
			assertEquals(expectedPoint.getX(), actualPoint.getX());
			assertEquals(expectedPoint.getY(), actualPoint.getY());
			assertEquals(expectedPoint.getColor(), actualPoint.getColor());
		}
	}
	
	/**
	 * Creates a 2D array of Color objects in which each object in the 2D array is the same.
	 * 
	 * @param width a positive integer
	 * @param height a positive integer
	 * @param color a color
	 * @return a width x height array of Color objects in which value of every element is color
	 * 
	 */
	private Color[][] createColorArray(final int width, final int height, final Color color) {
		Color[][] colors = new Color[height][width];
		
		for (int row = 0; row < height; row++) {
			for (int column = 0; column < width; column++) {
				colors[row][column] = color;
			}
		}
		
		return colors;
	}
	
	/**
	 * Asserts that the colors of the pixels of a Whiteboard object are the same as
	 * colors specified by a Color 2D array.
	 * 
	 * @param whiteboard a Whiteboard object
	 * @param colors the expected colors of the pixels in whiteboard
	 * 				 color is a whiteboard.getWidth() x whiteboard.getHeight() 2D array
	 * @throws an AssertionError only if the color of the pixel at (x, y) is not
	 * 		   colors[y][x]
	 */
	private void confirmColors(final Whiteboard whiteboard, final Color[][] colors) {
		assertEquals(whiteboard.getHeight(), colors.length);
		assertEquals(whiteboard.getWidth(), colors[0].length);
		
		for (int x = 0; x < whiteboard.getWidth(); x++) {
			for (int y = 0; y < whiteboard.getHeight(); y++) {
				if (!colors[y][x].equals(whiteboard.getColor(x, y))) {
					assertTrue(false);
				}
			}
		}
	}
}
