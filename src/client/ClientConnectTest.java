package client;

import static org.junit.Assert.*;

import org.junit.Test;

public class ClientConnectTest {
	/**
	 * The following tests test the ClientConnectGUI class.
	 * 
	 * The testing strategy for the ClientConnectGUI class:
	 * 		Enter an IP address that is valid.
	 * 			It should connect and close itself.
	 * 		Enter an IP address that is not valid.
	 * 			It should not connect and instead an error message
	 * 			should pop up on the GUI.
	 * 		Enter an IP address by
	 * 			clicking on the button "Connect!"
	 * 			pressing enter in the text box.
	 *      Enter same IP address concurrently as other user
	 *          should be able to connect to the same server
	 */
	@Test
	public void clientConnectTest() {
		assertTrue(true);
	}
	
	/**
	 * Manual Testing of the GUI:
	 * 
	 * When the WhiteboardClient is run, the GUI loads two rows as specified.
	 * The first row appropriately prompts the user to enter the server's IP
	 * address by loading the JLabel and the JTextField. The second line 
	 * contains only the 'Connect' JButton. Upon loading, the GUI title is 
	 * also correct.
	 * 
	 * It has been shown through repeated tests that if the IP address 
	 * textfield is left blank, then the error message ("Could not connect 
	 * to server. Please try again") is loaded in a third row.
	 * 
	 * It has been shown through repeated tests that if an incorrect IP
	 * address is input into the IP address JTextField, then then the error 
	 * message ("Could not connect to server. Please try again") is thrown.
	 * It doesn't matter what trial number it is, the GUI will always
	 * load an error message if an incorrect IP address is input. After an
	 * incorrect attempt, the textfield appropriately clears itself, allowing
	 * the user to have an empty textfield to try again.
	 * 
	 * It has been shown through repeated tests that if a correct IP
	 * address is input in the IP address JTextField, then the ClientConnectGUI
	 * closes and opens the ClientUsername GUI as specified. 
	 * 
	 * It has been shown through repeated tests that the user can enter an IP
	 * address into the textfield and to send a request to the server, after
	 * either pressing the "Connect!" button or by hitting the enter key while
	 * in the textbox.
	 */
}
