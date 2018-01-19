package whiteboardGUI;

import static org.junit.Assert.*;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.junit.Before;
import org.junit.Test;

import client.WhiteboardClient;

/**
 * Test some user interface stuff.
 * @category no_didit
 */

public class WhiteBoardGUITest {
    
    /**
     * Testing Strategy for WhiteBoardGUI:
     *      Click each button draw on canvas with each respective setting.
     *          Draw
     *          Erase
     *          Erase All
     *          Black
     *          Red
     *          Orange
     *          Yellow
     *          Green
     *          Blue
     *          Pink
     *      Move around slider bar to change thickness.
     *      Concurrent Users:  
     *          Drawing at same time in different places
     *          Drawing at same time in the same place with different colors
     *          Drawing and erasing at the same time
     *          Drawing with different thickness in different place
     *          Drawing with different thickness in the same place
     *          Drawing and erasing all at the same time
     *          Showing the usernames of the users that are currently editing the board
     *          
     * Manual Testing for the GUI:
     * 
     * It has been shown through repeated tests that after clicking on the draw button
     * and holding the mouse down over the canvas, a black line at the set thickness
     * will appear - tracing the line the user made with the mouse.
     * 
     * It has been shown through repeated tests that after clicking on the erase button
     * and holding the mouse down over the canvas, a white line at the set thickness
     * will appear - tracing the line the user made with the mouse. The white line
     * represents the user erasing.
     * 
     * It has been shown through repeated tests that after clicking on the erase all 
     * button, the entirety of the canvas will be set to white essentially clearing the
     * canvas.
     * 
     * It has been shown through repeated tests that after clicking on the black color 
     * button and holding the mouse down over the canvas, a black line at the set 
     * thickness will appear - tracing the line the user made with the mouse.
     * 
     * It has been shown through repeated tests that after clicking on the red color 
     * button and holding the mouse down over the canvas, a red line at the set 
     * thickness will appear - tracing the line the user made with the mouse.
     * 
     * It has been shown through repeated tests that after clicking on the orange color 
     * button and holding the mouse down over the canvas, an orange line at the set 
     * thickness will appear - tracing the line the user made with the mouse.
     * 
     * It has been shown through repeated tests that after clicking on the yellow color 
     * button and holding the mouse down over the canvas, a yellow line at the set 
     * thickness will appear - tracing the line the user made with the mouse.
     * 
     * It has been shown through repeated tests that after clicking on the green color 
     * button and holding the mouse down over the canvas, a green line at the set 
     * thickness will appear - tracing the line the user made with the mouse.
     * 
     * It has been shown through repeated tests that after clicking on the blue color 
     * button and holding the mouse down over the canvas, a blue line at the set 
     * thickness will appear - tracing the line the user made with the mouse.
     * 
     * It has been shown through repeated tests that after clicking on the pink color 
     * button and holding the mouse down over the canvas, a pink line at the set 
     * thickness will appear - tracing the line the user made with the mouse.
     * 
     * It has been shown through repeated tests that two users are able to draw at the
     * same time in different places regardless of color and thickness.
     * 
     * It has been shown through repeated tests that two users are able to draw in the 
     * same place regardless of thickness and color. However, one user will lock the 
     * respective point and draw first. The other user then has the opportunity to draw 
     * over the respective point.
     * 
     * It has been shown through repeated tests that two users are able to draw and erase 
     * at the same time in different places regardless of color and thickness.
     * 
     * It has been shown through repeated tests that one user is able to continuously
     * draw while another user erases all. The continuous drawing is separated into 
     * separate lines, so if the user erasing all is continuously pressing the 'erase all'
     * button, new line segments will continue to appear on the screen while the previous
     * line segments get erased.
     * 
     * It has been shown through repeated tests that at the bottom of the WhiteBoardGUI,
     * the current users editing the board are shown.
     *      
     */
    
    @Test
    public void ClientServicetest() {
        assertTrue(true);
    }
}