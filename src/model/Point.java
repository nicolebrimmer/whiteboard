package model;

import java.awt.Color;

/**
 * Point is an mutable, threadsafe datatype that represents a point in a 
 * window.
 * 
 * Abstraction Function:
 * 		Each point in a window is represented by:
 * 			x:     its x-coordinate
 * 			y:     its y-coordinate
 * 			color: its color
 * 
 * Representation Invariant:
 * 		Immutable
 * 
 * Thread safety Argument:
 * 		All read and write methods of the mutable portion of the Point representation 
 * 		(the color field) are synchronized on this Point object.  Since each
 * 		synchronized method requires a lock on only one Point object, this locking
 * 		mechanism will not produce a deadlock.
 * 
 * 		All read methods of the immutable portion of the Point representation (the x
 * 		and y fields) are not synchronized since they only access constants.
 * 
 */
public class Point {
	private final int x;
	private final int y;
	private Color color;
	
	/**
	 * Creates a Point object.
	 * 
	 * @param x the x-coordinate of the point in a window, x is nonnegative
	 * @param y the y-coordinate of the point in a window, y is nonnegative
	 */
	public Point(final int x, final int y, final Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	/**
	 * Returns the x-coordinate of the Point object.
	 * 
	 * @return x-coordinate of the Point object.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Returns the y-coordinate of the Point object.
	 * 
	 * @return y-coordinate of the Point object.
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Returns the color of the Point object.
	 * 
	 * @return the color of the Point object
	 */
	public synchronized Color getColor() {
		return color;
	}
	
	/**
	 * Changes the color of the Point object.
	 * 
	 * @param newColor the new color of the Point object
	 * @return the string representation of this Point object
	 */
	public synchronized String setColor(Color newColor) {
		color = newColor;
		return toString();
	}
	
	/**
	 * Returns the String representation of this Point object.
	 * 
	 * @return the String representation of this Point object which is of the form:
	 * 		   		[x] [y] [red] [green] [blue]	where [x] is the x-coordinate of this Point object
	 * 									  				  [y] is the y-coordinate of this Point object
	 * 									  				  [red] is the red component of the color of this Point object
	 * 													  [green] is the green component of the color of this Point object
	 * 													  [blue] is the blue component of the color of this Point object
	 */
	@Override
	public synchronized String toString() {
		return x + " " + y + " " + color.getRed() + " " + color.getGreen() + " " + color.getBlue();
	}
}
