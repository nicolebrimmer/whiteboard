package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Whiteboard is a mutable, threadsafe datatype that represents a collaborative
 * whiteboard.
 * 
 * Abstraction Function:
 * 		Each whiteboard is represented by:
 * 			name:      the name of the whiteboard
 * 			height:    the height of the whiteboard in pixels
 * 			width:     the width of the whiteboard in pixels
 * 			pixels:    a 2D array of points, each Point object representing a pixel in the whiteboard
 * 			usernames: the usernames of all of the clients that have this whiteboard open
 * 
 * Representation Invariant:
 * 		pixels is a height x width 2D array.
 * 		The Point object at pixels[y][x] represents the pixel at (x, y) where (0, 0) is the top
 * 		left hand corner of the whiteboard and (width - 1, height - 1) is the bottom right hand corner 
 * 		of the whiteboard.
 * 		Each pixel in the whiteboard is represented by a Point object in pixels.
 * 		pixels[y][x] always refers to the same Point object.
 * 		Each whiteboard must have a different name.
 * 		usernames contains the usernames of all of the clients that currently have this whiteboard open.
 * 		The order of the usernames in usernames is the order that the client connected (i.e. the order that the
 * 		addUsername() method was called).
 * 
 * Thread safety Argument:
 * 		The height, width and the name fields of each Whiteboard object are of immutable types and are also private 
 * 		and final.  In other words, these fields are constant and do not change after the Whiteboard object is created.
 * 		Therefore, none of the read methods for these fields are synchronized.
 * 		
 * 		The getColor(x, y) and setColor(x, y, newColor) methods call the corresponding methods in the 
 * 		Point class, which are synchronized on the Point object located at pixels[y][x].  Since pixels[y][x] 
 * 		always refers to the same Point object, all calls on getColor(x, y) and setColor(x, y) will be synchronized on
 * 		the same Point object at pixels[y][x].  In other words, only one thread can read or write a given pixel
 * 		at any time.  And since setColor(x, y) also returns the string representation of the newly modified pixel at 
 * 		(x, y), there can be no race condition between changing the color of the pixel and getting the string
 * 		representation of the new pixel.
 * 
 * 		In addition the resetWhiteboard() method calls the corresponding methods in the Point class, which are once
 * 		again synchronized on each individual Point object in the whiteboard.  So once again, only one thread
 * 		can read or write a given pixel at any time.
 * 
 * 		The addLine() method is synchronized on the Whiteboard, meaning that only one client can request a change to the
 * 		whiteboard at a particular time, eliminating the race condition that two clients could draw a line and at each point
 * 		a different client could win, leading in a checkerboard line.
 * 
 * 		All reads and writes to usernames are synchronized on this whiteboard.  In addition, each write to usernames
 * 		returns the new string representation of usernames, so there can be no race condition between changing
 * 		usernames and getting the string representation of the usernames.
 * 
 * 		All synchronized methods obtains at most one lock at a given time during runtime - either a lock on the 
 * 		whiteboard or a lock on a Point object.  Therefor, this locking mechanism will not produce a deadlock.
 *
 */
public class Whiteboard {
	private final String name;
	private final int height;
	private final int width;
	private final Point[][] pixels;
	private final ArrayList<String> usernames; 
	
	/**
	 * Creates an empty Whiteboard object.
	 * 
	 * @param name the name of the whiteboard, each Whiteboard object must have a different name
	 * @param width the width of the whiteboard in pixels, must be positive
	 * @param height the height of the whiteboard in pixels, must be positive
	 */
	public Whiteboard(final String name, final int width, final int height) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.pixels = new Point[height][width];
		this.usernames = new ArrayList<String>();
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				pixels[y][x] = new Point(x, y, Color.WHITE);
			}
		}
	}
	
	/**
	 * Returns a string containing the usernames of all of the clients that currently have this whiteboard open
	 * separated by single spaces.
	 * 
	 * @return a string containing the usernames of all of the clients that currently have this whiteboard 
	 * 		   open, separated by a single space.
	 */
	public synchronized String getUsernames() {
		String usernameString = "";
		
		for (int i = 0; i < usernames.size(); i++) {
			usernameString += usernames.get(i);
			
			// As long as this username is not the last username in the list,...
			if(i != usernames.size() - 1) {
				usernameString += " ";
			}
		}
		
		return usernameString;
	}
	
	/**
	 * Adds a username to this whiteboard, signifying that a client has connected to this whiteboard.
	 * 
	 * @param username the username of the client that has opened this whiteboard.  
	 * 				   This username must be unique (i.e. each client must have a different username).
	 * @return a string containing the usernames of all of the clients that currently have this whiteboard 
	 * 		   open, separated by a single space
	 * 
	 */
	public synchronized String addUsername(final String username) {
		usernames.add(username);
		return getUsernames();
	}
	
	/**
	 * Removes a username from this whiteboard, signifying that a client has disconnected from this whiteboard.
	 * 
	 * @param username the username of the client that has disconnected from this whiteboard.  The client whose username
	 * 				   is username must be connected to this whiteboard when this method is called
	 * @return a string containing the usernames of all of the clients that currently have this whiteboard 
	 * 		   open, separated by a single space
	 * 
	 */
	public synchronized String removeUsername(final String username) {
		usernames.remove(username);
		return getUsernames();
	}
	
	/**
	 * Indicates whether a particular client whose username is username is currently connected to this whiteboard.
	 * 
	 * @param username the username of a client of the WhiteboardServer
	 * @return true only if the client whose username is username is connected to the server.
	 * 
	 */
	public synchronized boolean hasUsername(final String username) {
		return usernames.contains(username);
	}
	
	/**
	 * Returns the name of the whiteboard object.
	 * 
	 * @return name of this whiteboard object.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the height of the whiteboard in pixels.
	 * 
	 * @return height of this Whiteboard object in pixels
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Returns the width of the whiteboard in pixels.
	 * 
	 * @return width of this Whiteboard object in pixels
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Changes the color of a pixel in the whiteboard.
	 * 
	 * @param x the x-coordinate of the pixel to be changed
	 * 			0 <= x < width
	 * @param y the y-coordinate of the pixel to be changed
	 * 			0 <= y < height
	 * @return the String representation of the Point object that is changed
	 * 
	 */
	public String setColor(final int x, final int y, final Color newColor) {
		Point point = pixels[y][x];
		return point.setColor(newColor);
	}
	
	/**
	 * Returns the color of a pixel in the whiteboard.
	 * 
	 * @param x the x-coordinate of the pixel
	 * 			0 <= x < width
	 * @param y the y-coordinate of the pixel
	 * 			0 <= y < height
	 * @return the color of the pixel at (x, y)
	 */
	public Color getColor(final int x, final int y) {
		Point point = pixels[y][x];
		return point.getColor();
	}
	
	/**
	 * Adds a line segment to this Whiteboard object.
	 * 
	 * @param color the color of the line segment
	 * @param x1 the x-coordinate of the starting point of the line segment
	 * 			 0 <= x1 < width
	 * @param y2 the y-coordinate of the starting point of the line segment
	 * 			 0 <= y1 < height
	 * @param x2 the x-coordinate of the ending point of the line segment
	 * 			 0 <= x1 < width
	 * @param y2 the y-coordinate of the ending point of the line segment
	 * 			 0 <= y2 < height
	 * @param thickness the thickness of the line segment in pixels
	 * @return the String representation of the line segment that has just been added to this 
	 * 		   whiteboard.  The String representation of the line segment contains n lines where n is 
	 * 		   the number of points in the line segment and each line in this String representation is the
	 * 		   the string representation of a point in the line segment.
	 * @modifies adds a line segment to the whiteboard that starts at (x1, y1) and ends
	 * 			 at (x2, y2) and whose thickness in pixels is thickness
	 * 
	 */
	public String addLine (final Color color, final int x1, final int y1, final int x2, final int y2, final int thickness) {
		ArrayList<Point> pointsOnLine = getPointsOnLine(color, x1, y1, x2, y2);		
		ArrayList<Point>  points = new ArrayList<Point>();
		
		boolean[][] bmatrix = new boolean[width][height];	
		for(int i = 0; i<width; i++){
			for(int y = 0; y<height; y++){
				bmatrix[i][y] = false;
			}
		}
		// Thickens the line defined by pointsOnLine, the interpolation of (x1, y1) and (x2, y2)
		int halfWidth = thickness / 2;
		int xCoordinate, yCoordinate;
		int xMin, xMax, yMin, yMax;
		for (int i = 0; i < pointsOnLine.size(); i++) {
			xCoordinate = pointsOnLine.get(i).getX();
			yCoordinate = pointsOnLine.get(i).getY();
			
			xMin = Math.max(0, xCoordinate - halfWidth);
			xMax = Math.min(width - 1, xCoordinate + halfWidth);
			yMin = Math.max(0, yCoordinate - halfWidth);
			yMax = Math.min(height - 1, yCoordinate + halfWidth);
			
			for (int x = xMin; x <= xMax; x++) {
				for (int y = yMin; y <= yMax; y++) {
					if(bmatrix[x][y] == true){
						continue;
					}
					points.add(new Point(x, y, color));
					bmatrix[x][y] = true;
				}
			}
		}
		
		// Color the specified points
		return colorPoints(points, color);
	}
	
	/**
	 * Determines all of the points between the starting and ending points of a line segment.
	 * 
	 * @param x1 the x-coordinate of the starting point of the line segment
	 * 			 0 <= x1 < width
	 * @param y2 the y-coordinate of the starting point of the line segment
	 * 			 0 <= y1 < height
	 * @param x2 the x-coordinate of the ending point of the line segment
	 * 			 0 <= x1 < width
	 * @param y2 the y-coordinate of the ending point of the line segment
	 * 			 0 <= y2 < height
	 * @param color the color of the line segment
	 * @return a list containing all of the points on the line segment connecting (x1, y1) and (x2, y2)
	 * 		   in the order of moving from (x1, y1) to (x2, y2)
	 * 
	 */
	protected static ArrayList<Point> getPointsOnLine (final Color color, final int x1, final int y1, final int x2, final int y2) {
		ArrayList<Point> pointsOnLine = new ArrayList<Point>();
		
		// A vertical line does not have a defined slope
		if (x1 == x2) {
			int incrementer;
			if (y2 < y1) { // If you are moving down vertically
				incrementer = -1;
			}
			else {
				incrementer = 1;
			}
			
			final int xCoordinate = x1;
			for (int yCoordinate = y1; yCoordinate != y2; yCoordinate += incrementer) {
				pointsOnLine.add(new Point(xCoordinate, yCoordinate, color));
			}
		}
		else {			
			final float slope = ((float) (y2 - y1)) / ((float) (x2 - x1));
		
			// If the line is very steeply sloped, add one point for every y-coordinate.
			if (Math.abs(slope) >= 1) {
				float xCoordinate = x1;
				float xCoordinateIncrementer = ((float) (x2 - x1) / (float) (y2 - y1));
				
				int yCoordinateIncrementer;
				if (y2 < y1) { // moving down
					yCoordinateIncrementer = -1;
					xCoordinateIncrementer *= -1;
				}
				else { // moving up
					yCoordinateIncrementer = 1;
				}
				
				for (int yCoordinate = y1; yCoordinate != y2; yCoordinate += yCoordinateIncrementer) {
					pointsOnLine.add(new Point(Math.round(xCoordinate), yCoordinate, color));
					xCoordinate = xCoordinate + xCoordinateIncrementer;
				}
				
			}
			
			// If the line is not very steeply slope, add one point for every x-coordinate.
			else {
				float yCoordinate = y1;
				float yCoordinateIncrementer = slope;
				
				int xCoordinateIncrementer;
				if (x2 < x1) { // If you are moving to the left
					xCoordinateIncrementer = -1;
					yCoordinateIncrementer *= -1;
				}
				else {
					xCoordinateIncrementer = 1;
				}
				
				for (int xCoordinate = x1; xCoordinate != x2; xCoordinate += xCoordinateIncrementer) {
					pointsOnLine.add(new Point(xCoordinate, (int) yCoordinate, color));
					yCoordinate = yCoordinate + yCoordinateIncrementer;
				}
			}
		}
		
		pointsOnLine.add(new Point(x2, y2, color));
		
		return pointsOnLine;
	}
	
	/**
	 * Colors all of the points specified with the specified color
	 * 
	 * @param pointsOnThickLine the points in this Whiteboard object to be colored;
	 * 							each Point object, point, in pointsOnThickLine must be
	 * 							in this Whiteboard object, i.e. 0 <= point.getX() < width
	 * 							and 0 <= point.getY() < height
	 * @param color the color that the points in pointsOnThickLine must be colored;
	 * 				the color of the each of the Point objects in pointsOnThickLine must be color
	 * @return a String consisting of n lines, where n is the number of points in pointsOnThickLine
	 * 		   each line in the String is the String representation 
	 * @modifies colors all of the points in pointsOnThickLine with color
	 * 
	 */
	protected String colorPoints (ArrayList<Point> points, Color color) {
		String line = "";
		
		for (int i = 0; i<points.size(); i++) {
			Point currentPoint = points.get(i);
			
			int xCoordinate = currentPoint.getX();
			int yCoordinate = currentPoint.getY();
			
			line += setColor(xCoordinate, yCoordinate, color) + " ";
		}
		
		return line;
	}
	
	/**
	 * Clears the whiteboard.
	 * 
	 * @modifies makes the whiteboard completely white
	 */
	public void clearWhiteboard() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				pixels[y][x].setColor(Color.WHITE);
			}
		}
	}
	
	/**
	 * Returns the string representation of this Whiteboard
	 * 
	 * @return the String representation of this Whiteboard which consists the string representations
	 * 		   of all of the pixels in this Whiteboard that are not white, each one of which separated by a space.
	 */
	@Override
	public synchronized String toString() {
		String representation = "";
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				Point currentPoint = pixels[y][x];
				if (!Color.WHITE.equals(currentPoint.getColor())) {
					representation += currentPoint.toString() + " ";
				}
			}
		}
		
		return representation;
	}
}
