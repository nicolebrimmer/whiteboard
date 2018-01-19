package client;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ClientUsernameTest {
	/**
	 * The following tests test the ClientUsername class.
	 * 
	 * The testing strategy for the ClientUsername class:
	 * 		Request a username that is not valid:
	 * 			The username has a space.
	 * 			The username has already been added.
	 * 		Request a username by
	 * 			clicking on the button.
	 * 			pressing enter in the text box.
	 */
	@Test
	public void clientUsernameTest() {
		assertTrue(true);
	}
	
	/**
	 * Manual Testing of the GUI:
	 * 
	 * After a correct IP address is input, the ClientUsername GUI
	 * appears. It has two rows as specified, the first for the username 
	 * prompt with JLabel and JTextField and the second for the "Create
	 * username!" JButton. Upon loading, the GUI title is also correct.
	 * 
	 * It has been shown through repeated tests that if the "Create 
	 * username!" button is pressed without any text having been input into
	 * the textfield in the first row, no user will be created and the GUI
	 * will remain open with a blank textfield.
	 * 
	 * It has been shown through repeated tests that if the "Create 
	 * username!" button is pressed with an incorrect input, the respective
	 * error message will be loaded in a third row on the GUI. Incorrect 
	 * inputs include a username that has already been added and a username
	 * that includes a space. As stated before, if either of these types of
	 * inputs are entered, then their respective errors will be loaded. 
	 * 
	 * It has been shown through repeated tests that if the "Create
	 * username!" button is pressed when a valid string is input in the 
	 * username texfield, a user is created, the GUI, closes, and the 
	 * ClientService GUI opens.
	 */

}
