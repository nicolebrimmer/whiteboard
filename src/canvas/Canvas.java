package canvas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import client.WhiteboardClient;

/**
 * Canvas represents a drawing surface that allows the user to draw
 * on it freehand, with the mouse.
 * 
 * Abstraction Function:
 * 		drawingBuffer - the drawing buffer for this canvas.  Everything is first drawn
 * 						to the drawing buffer and is then copied over to the canvas to be
 * 						displayed
 * 		currentColor  - the color of lines that are added to the canvas
 * 		sWidth        - the thickness of lines that are added to the canvas
 * 		client        - the WhiteboardClient that opened up this canvas
 * 
 * Representation Invariant:
 * 		The canvas displays the whiteboard that the client requested as it is updated both by
 * 		this client and other clients.
 * 
 */
public class Canvas extends JPanel {

	private static final long serialVersionUID = -4184358272707176669L;
	
	// image where the user's drawing is stored
    private Image drawingBuffer;
    public static Color currentColor = Color.BLACK;
    public int sWidth;
    private final WhiteboardClient client;
    private String whiteboardRep;
    private String whiteboardName;
    
    /**
     * Make a canvas.
     * 
     * @param width width in pixels
     * @param height height in pixels
     * @param client the WhiteboardClient that opened this canvas.
     * @param initialWhiteboard a string representation of the initial whiteboard
     * @param whiteboardName the name of the whiteboard that is displayed by this canvas
     * 
     */
    public Canvas(int width, int height, WhiteboardClient client, String initialWhiteboard, String whiteboardName) {
        addDrawingController();
        this.client = client;
        this.whiteboardRep = initialWhiteboard;
        this.whiteboardName = whiteboardName;
        
        // note: we can't call makeDrawingBuffer here, because it only
        // works *after* this canvas has been added to a window.  Have to
        // wait until paintComponent() is first called.
    }
    
    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        // If this is the first time paintComponent() is being called,
        // make our drawing buffer.
        if (drawingBuffer == null) {
            makeDrawingBuffer();
        }
        
        // Copy the drawing buffer to the screen.
        g.drawImage(drawingBuffer, 0, 0, null);
    }
    
    /**
     * Make the drawing buffer and draw some starting content for it.
     * 
     */
    private void makeDrawingBuffer() {
        drawingBuffer = createImage(getWidth(), getHeight());
        fillWithWhite();
        addPoints(whiteboardRep);
    }
    
    /**
     * Make the drawing buffer entirely white.
     * 
     */
    public void fillWithWhite() {
        final Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();
        
        g.setColor(Color.WHITE);
        g.fillRect(0,  0,  getWidth(), getHeight());
        
        // IMPORTANT!  every time we draw on the internal drawing buffer, we
        // have to notify Swing to repaint this component on the screen.
        this.repaint();
    }
    
    /**
     * Draw the series of points represented by the inputted string onto the canvas.
     * 
     * @param line a String representing a series of points to be added to the canvas
     * 			   line is of the forming form:
     * 					([x] [y] [red] [green] [blue])* indicating that the pixel at (x, y)
     * 					should be colored new Color([red], [green], [blue])
     * @modifies draw line onto the canvas.
     * 
     */
    public void addPoints(final String line) {
    	if (!line.equals("")) {
            Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();
        	String[] points = line.split(" ");
        	
        	int i = 0;
        	while (i < points.length) {
        		int x = Integer.parseInt(points[i]); i++;
        		int y = Integer.parseInt(points[i]); i++;
        		
        		int red = Integer.parseInt(points[i]); i++;
        		int green = Integer.parseInt(points[i]); i++;
        		int blue = Integer.parseInt(points[i]); i++;
        		Color color = new Color(red, green, blue);

                g.setStroke(new BasicStroke(1));
                
                g.setColor(color);
                g.drawLine(x, y, x, y);
        	}
        	
        	this.repaint();
    	}
    }
    
    
    /*
     * Draw a happy smile on the drawing buffer.
     */
//    private void drawSmile() {
//        final Graphics2D g = (Graphics2D) drawingBuffer.getGraphics();
//
//        // all positions and sizes below are in pixels
//        final Rectangle smileBox = new Rectangle(20, 20, 100, 100); // x, y, width, height
//        final Point smileCenter = new Point(smileBox.x + smileBox.width/2, smileBox.y + smileBox.height/2);
//        final int smileStrokeWidth = 3;
//        final Dimension eyeSize = new Dimension(9, 9);
//        final Dimension eyeOffset = new Dimension(smileBox.width/6, smileBox.height/6);
//        
//        g.setColor(Color.BLACK);
//        g.setStroke(new BasicStroke(smileStrokeWidth));
//        
//        // draw the smile -- an arc inscribed in smileBox, starting at -30 degrees (southeast)
//        // and covering 120 degrees
//        g.drawArc(smileBox.x, smileBox.y, smileBox.width, smileBox.height, -30, -120);
//        
//        // draw some eyes to make it look like a smile rather than an arc
//        for (int side: new int[] { -1, 1 }) {
//            g.fillOval(smileCenter.x + side * eyeOffset.width - eyeSize.width/2,
//                       smileCenter.y - eyeOffset.height - eyeSize.width/2,
//                       eyeSize.width,
//                       eyeSize.height);
//        }
//        
//        // IMPORTANT!  every time we draw on the internal drawing buffer, we
//        // have to notify Swing to repaint this component on the screen.
//        this.repaint();
//    }
    
    /**
     * Draw a line between two points (x1, y1) and (x2, y2), specified in
     * pixels relative to the upper-left corner of the drawing buffer.
     */
    private void drawLineSegment(int x1, int y1, int x2, int y2) {
    	client.sendMessage("draw " + whiteboardName + " " + x1 + " " + y1 + " " + x2 + " " + y2 + " "
    					   + currentColor.getRed() + " " + currentColor.getGreen() + " "
    					   + currentColor.getBlue() + " " + sWidth);
    }
    
    /**
     * Add the mouse listener that supports the user's freehand drawing.
     */
    private void addDrawingController() {
        DrawingController controller = new DrawingController();
        addMouseListener(controller);
        addMouseMotionListener(controller);
    }
    
    /**
     * Sets the color of the lines to be drawn in this canvas.
     * 
     * @param color the color of the lines to be drawn in this canvas.
     */
    public static void setColor(Color color){
    	currentColor = color;
    }
    
    /*
     * DrawingController handles the user's freehand drawing.
     */
    private class DrawingController implements MouseListener, MouseMotionListener {
        // store the coordinates of the last mouse event, so we can
        // draw a line segment from that last point to the point of the next mouse event.
        private int lastX, lastY; 

        /*
         * When mouse button is pressed down, start drawing.
         */
        public void mousePressed(MouseEvent e) {
            lastX = e.getX();
            lastY = e.getY();
        }

        /*
         * When mouse moves while a button is pressed down,
         * draw a line segment.
         */
        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            drawLineSegment(lastX, lastY, x, y);
            lastX = x;
            lastY = y;
        }

        // Ignore all these other mouse events.
        public void mouseMoved(MouseEvent e) { }
        public void mouseClicked(MouseEvent e) { }
        public void mouseReleased(MouseEvent e) { }
        public void mouseEntered(MouseEvent e) { }
        public void mouseExited(MouseEvent e) { }
    }
}
