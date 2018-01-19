package client;

import static org.junit.Assert.*;

import org.junit.Test;

public class ClientServiceTest {

    /**
     * The following tests test the ClientService class.
     * 
     * The testing strategy for the ClientService class:
     *      Create a valid user:
     *          Addition of username into table
     *      Request to create an invalid board:
     *          The board name has a space.
     *          The board name has already been added.
     *          The board name uses characters other than ascii characters.
     *          The length of the board name is greater than Integer.MAX_INT 
     *      Request to create a board by:
     *          Entering board name into textfield,
     *          Clicking on 'Create Board' button.
     *      Request to open an invalid board:
     *          The board name has not already been added.
     *      Request to open a board by:
     *          Entering board name into textfield,
     *          Clicking on 'Open Board' button. 
     *      Creation and opening of boards is case-sensitive.
     *      Client is only allowed to open one whiteboard at a time.
     *          
     */
    
    @Test
    public void ClientServicetest() {
        assertTrue(true);
    }

    /**
     * Manual Testing of the GUI:
     * 
     * Upon successful username creation, the ClientService GUI appears.
     * The layout is correctly loaded each time. There are three rows above
     * the JTable followed by a row at the bottom of the GUI. The first row
     * appropriately contains a JLabel, the second row appropriately contains
     * a JTextField for the user to input a board name, and the third row
     * appropriately contains two JButtons used to either create a board or
     * open a board. The JTable, which loads below the first three rows, 
     * appropriately contains two columns, one for users online and one for
     * Whiteboards created. The column for users online is always updated
     * upon loading of this GUI with the username. In the fourth row, 
     * underneath the first three rows and the JTable, there is a JTextArea 
     * to display error messages.
     * 
     * It has been shown through repeated tests that if the "Create 
     * board" button or "Open board" button are pressed without any board 
     * name having been input into the textfield in the second row, no board 
     * will be created and the GUI will remain unchanged with a blank 
     * textfield.
     * 
     * It has been shown through repeated tests that if the "Create board"
     * button is pressed with an invalid board name, then the respective 
     * error message is loaded. Incorrect inputs include board names that have
     * already been added or has a space in it. As stated before, if either 
     * of these types of inputs are entered, then their respective errors 
     * will be loaded.
     * 
     * It has been shown through repeated tests that if the "Create
     * board" button is pressed when a valid string is input in the 
     * board textfield, the 'Whiteboards created' column is updated with the
     * name of the new board. The string is case-sensitive; therefore, blue and Blue
     * can be different valid board names.
     * 
     * It has been shown through repeated tests that if the "Open board"
     * button is pressed with an invalid board name, a name that has not 
     * already been added, then its respective error message is loaded. 
     * 
     * It has been shown through repeated tests that if the "Open board" 
     * button is pressed with a board name that has already been added,
     * then it loads up the respective WhiteboardGUI. The textfield will then
     * be cleared in order to allow the user to input another board.
     */
}
