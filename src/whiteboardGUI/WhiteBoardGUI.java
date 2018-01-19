package whiteboardGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import client.WhiteboardClient;

import canvas.Canvas;

/**
 * This class builds out the GUI that is the editable surface that the client will work on. 
 * 
 * Abstraction Function:
 * 			client - the username of the client that is tied to this particular whiteboard
 * 
 * 			whiteboardName - the name of the whiteboard.
 * 
 * Representation Invariant
 * 			This window can change the type of writing, the color of writing and the size of writing.
 * 
 * Thread Safety Argument:
 * 			
 * 			This is thread safe by confinement. 
 * 
 * 			Each line drawn on the whiteboard is not drawn locally. It is sent to the server which 
 * 			changes its representation of the whiteboard and then sends that back to the local version
 * 			for an update.
 *			
 * 
 *
 */

public class WhiteBoardGUI extends JFrame {
	private static final long serialVersionUID = -5393918641159899887L;
	private final int STK_MIN = 1;
	private final int STK_MAX = 30;
	private final int STK_INIT = 1;
	
	private final JPanel contentPane;
	private final JToolBar toolBar;
	private final JSlider strokeWidth = new JSlider (JSlider.HORIZONTAL, STK_MIN, STK_MAX, STK_INIT);;
	private final Canvas canvas;
	private final JButton drawButton = new JButton("Draw"); 
	private final JButton eraserButton = new JButton("Erase");
	private final JButton clearBoardButton = new JButton("Erase All");
	private JButton baColor = new JButton("    ");
	private JButton rColor = new JButton("    ");
	private JButton oColor = new JButton("    ");
	private JButton yColor = new JButton("    "); 
	private JButton gColor = new JButton("    "); 
	private JButton blColor = new JButton("    "); 
	private JButton pColor = new JButton("    ");
	private final JLabel usersOnline;
	private final WhiteboardClient client;
	private final String whiteboardName;
	private final ArrayList<JButton> barButtons = new ArrayList<JButton>(Arrays.asList(drawButton, eraserButton, clearBoardButton, baColor, rColor, oColor, yColor, gColor, blColor, pColor));
	private final ArrayList<Color> buttonColors = new ArrayList<Color>(Arrays.asList(Color.BLACK, Color.WHITE, Color.WHITE, Color.BLACK, Color.RED, Color.orange, 
			Color.YELLOW, Color.GREEN, Color.BLUE, Color.PINK));


	/**
	 * Creates a WhiteBoardGUI object
	 * 
	 * @param client the client that this WhiteBoardGUI object serves.
	 * @param whiteboardName the name of the whiteboard that this GUI displays
	 * @param initialWhiteboard the String representation of the initial whiteboard
	 */
	public WhiteBoardGUI (WhiteboardClient client, String whiteboardName, String initialWhiteboard) {
		this.client = client;
		this.whiteboardName = whiteboardName;
		
		this.setTitle("Whiteboard " + client.getWhiteboardName());
		this.setResizable(false);
		contentPane = new JPanel(new BorderLayout());
		
		toolBar = new JToolBar("Colors");
		
		canvas = new Canvas(800, 600, client, initialWhiteboard, whiteboardName);
		
		toolBar.setPreferredSize(new Dimension(600, 50));
		usersOnline = new JLabel();
		
		contentPane.setBorder(BorderFactory.createLineBorder(Color.black));
		contentPane.add(toolBar, BorderLayout.NORTH);
		contentPane.add(canvas, BorderLayout.CENTER);
		contentPane.add(usersOnline, BorderLayout.SOUTH);
		
		addToolBarButtons();
		addActionListenerOperationOnClose();
		addActionListeners();
		
		this.setPreferredSize(new Dimension(800, 600));
		this.getContentPane().add(contentPane);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.pack();
	}
	
	/**
	 * Adds buttons to a tool bar for changing between an eraser and a writing utensil, 
	 * a slider for the thickness of the lines drawn to the canvas and a series of buttons for
	 * changing the color of the lines drawn to the canvas. 
	 * 
	 */
	public void addToolBarButtons(){

		for(int i = 0; i < barButtons.size(); i++){
	        
	        JButton color = barButtons.get(i);
	       
	        //adds color to the swatches on the toolbar
	        if (i>2){
	        	Color pallet = buttonColors.get(i);
	        	color.setSize(new Dimension(10, 10));
	        	color.setBackground(pallet);
	        	color.setOpaque(true);
	        	color.setBorderPainted(false);
	        }
			toolBar.add(color);
	        toolBar.addSeparator(new Dimension(10,5));
		}
		strokeWidth.setMajorTickSpacing(15);
		strokeWidth.setPaintTicks(true);

		// Create the label table
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put( new Integer( 1 ), new JLabel("Thin") );
		labelTable.put( new Integer( 15 ), new JLabel("Medium") );
		labelTable.put( new Integer( STK_MAX ), new JLabel("Thick") );
		strokeWidth.setLabelTable( labelTable );

		strokeWidth.setPaintLabels(true);
		toolBar.add(strokeWidth);
	}
	
	/**
	 * Add action listeners to all of the buttons in the whiteboard window.
	 * 
	 */
	public void addActionListeners() {		

		for(int i = 0; i <barButtons.size(); i++) {
			JButton color = barButtons.get(i);
			Color pallet = buttonColors.get(i);
			final Color buttonColor = pallet;
			if(color.equals(clearBoardButton)){

				color.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e)
					{	
						client.sendMessage("reset " + whiteboardName);

					}
				});
			}
			//add action to the buttons and swatches
			color.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e)
				{	
					Canvas.setColor(buttonColor);

				}
			});
		}
		
			//adds action to the width slider
			strokeWidth.addChangeListener(new ChangeListener() {
			    
				@Override
				public void stateChanged(ChangeEvent e) {
			        JSlider strokeWidth = (JSlider) e.getSource();

			        int sWidth = strokeWidth.getValue();
			        	canvas.sWidth = sWidth;
				}
			});
		}
	
		
	
    /**
     * Adds an ActionListener to the closing of the WhiteBoardGUI window so that the client
     * is disconnected from this whiteboard.
     * 
     */
    public void addActionListenerOperationOnClose() {
    	WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
            	client.closeWhiteboardGUI();
                client.sendMessage("close " + client.getUsername() + " " + whiteboardName);
                dispose();
            }
        };
        addWindowListener(exitListener);
    }
	
	/**
	 * Adds a series of points to the canvas.
	 * 
	 * @param line the String representation of the series of points to be added to the canvas.
	 * 
	 */
	public void drawLine(final String line) {
		canvas.addPoints(line);
	}
	
	/**
	 * Update GUI to show the users online.
	 * 
	 * @param usersConnectedToWhiteboard a String containing all of the users connected to the whiteboard
	 * 									 separated by spaces.
	 */
	public void setUsersOnline(final String usersConnectedToWhiteboard) {
		usersOnline.setText("Users editing this whiteboard now: " + usersConnectedToWhiteboard);
	}
	
	/**
	 * Clears the canvas.
	 */
	public void clearCanvas() {
		canvas.fillWithWhite();  
	}
}
	
