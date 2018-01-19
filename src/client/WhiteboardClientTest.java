package client;

import static org.junit.Assert.*;
//import client.ClientService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import server.WhiteboardServer;

public class WhiteboardClientTest {
    
    public static final int port = 4444;
    private static AtomicInteger portIncrementer = new AtomicInteger(1);
    
    /**
     * The following test methods test the create command in the 
     * handleRequest method in the WhiteboardClient class.
     * 
     * Testing strategy for usernameerror:
     *      Receive message from server with form:
     *          usernameerror That username is already being used.
     *          usernameerror There should be no spaces in the username.
     *      Pass each of these usernameerror messages and ensure that an error message
     *      is loaded on the GUI.
     *      
     * Note that there are no test methods for this command.  All of the tests
     * for this command are manual. The user must be able to see the error
     * messages on the screen. Notice that this follows the testing strategy
     * described above.
     *      Type client.handleRequest("usernameerror That username is already being used.")
     *          See that an error message is loaded on the ClientUsername GUI
     *      Type client.handleRequest("usernameerror There should be no spaces in the username.")
     *          See that an error message is loaded on the ClientUsername GUI
     *          
     * 
     * Testing strategy for usernameCreated:
     *      Receive message from server with form: 
     *          usernameCreated [username]
     *      Pass a userCreated message and ensure that the ClientUsername GUI closes and
     *      the ClientService GUI opens with the client's username entered into the 
     *      'Current Users' column. 
     *      
     * Note that there are no test methods for this command.  All of the tests
     * for this command are manual. The user will be able to see a successful creation 
     * of a username if the ClientUsername GUI closes, giving rise to the ClientService
     * GUI. THe user will also be able to see the username of the GUI.
     *      Type client.handleRequest("usernameCreated blue")
     *          See that the ClientUsername GUI closes and the ClientService GUI opens with
     *          the username of the client added to the "Current Users" column
     * 
     * Testing strategy for allUsersOnline:
     *      Receive message from server with form:
     *          allUsersOnline [usernames]
     *      Pass allUsersOnline message to the client and ensure that all of the
     *      usernames appear in the GUI in the "Connected Users" column
     *          
     * Note that there are no test methods for this command.  All of the tests
     * for this command are manual. The user must be able to see all of the users online
     * in the ClientService GUI.
     *      Type client.handleRequest("usernameCreated blue")
     *      After creating a client and opening the ClientService GUI, 
     *          Type client.handleRequest("allUsersOnline red yellow green")
     *              See that the "Connected Users" column of the GUI displays the 
     *              respective users.
     *          
     * Testing strategy for whiteboardnameerror:
     *      Receive message from server with form:
     *          whiteboardnameerror Whiteboard names cannot contain any spaces.
     *          whiteboardnameerror A whiteboard with that name has already created.  Please choose another name.
     *      Pass each of these whiteboardnamerror messages to client and ensure that an 
     *      error message is loaded on the GUI.
     * 
     * Note that there are no test methods for this command.  All of the tests
     * for this command are manual. The user must be able to see the error
     * messages on the screen. Notice that this follows the testing strategy
     * described above.
     *      Type client.handleRequest("whiteboardnameerror Whiteboard names cannot contain any spaces.")
     *          See that an error message is loaded on the ClientService GUI
     *      Type client.handleRequest("whiteboardnameerror A whiteboard with that name has already created.  Please choose another name.")
     *          See that an error message is loaded on the ClientService GUI
     *              
     * Testing strategy for allwhiteboards:
     *      Receive message from the server with form:
     *          allwhiteboards [whiteboards]
     *      Pass allwhiteboards message to the client and ensure that all of the
     *      whiteboards created appear in the GUI in the "Whiteboards Created" column
     *          
     * Note that there are no test methods for this command.  All of the tests
     * for this command are manual. The user must be able to see all of the whiteboards 
     * created in the ClientService GUI.
     *      Type client.handleRequest("usernameCreated blue")
     *      After creating a client and opening the ClientService GUI, 
     *          Type client.handleRequest("allwhiteboards red yellow green")
     *              See that the "Whiteboards Created" column of the GUI displays the 
     *              respective users.
     *          
     * Testing strategy for open whiteboard:
     *      Receive message from the server with form:
     *          open [whiteboardname] [whiteboard]
     *      Pass open whiteboard message and ensure that a whiteboard is opened.
     *      Test both on empty whiteboards and on marked up whiteboards.
     *          
     * Note that there are no test methods for this command.  All of the tests
     * for this command are manual. The user must be able to open the respective 
     * whiteboard requested.
     *      Create clients on the server via the username [username] command on the 
     *      server side.
     *          Type 'username john'
     *          Type 'username joe'
     *      Create whiteboards via the create [name] command on the server side.
     *          Type 'create empty'
     *          Type 'create marked'
     *      Open whiteboard via the open [whiteboardname] [whiteboard]   
     *          Type client.handleRequest("open empty") 
     *      Have john draw on 'marked' board
     *          Type "draw marked 0 0 1 1 0 0 255 1" on the "john" client.
     *      Open whiteboard via the open [whiteboardname] [whiteboard]   
     *          Type client.handleRequest("open marked" + [string rep of board])
     *          
     * Testing strategy for alsoediting:
     *      Receive message from server in form:
     *          alsoediting [usernames]
     *      Create a client via server side commands. Create a Whiteboard. Open the 
     *      Whiteboard. Then pass an alsoediting message with multiple other usernames and
     *      ensure that they appeared on the GUI.
     *      
     * Note that there are no test methods for this command.  All of the tests
     * for this command are manual. The user must be able to see the other users editing
     * the WhiteBoardGUI at the same time.
     *      Create clients on the server via the username [username] command on the 
     *      server side.
     *          Type 'username john'
     *      Create a list of users that are also editing the same WhiteboardGUI
     *          Type client.handlerequest("alsoediting [joe blow clow drow]")
     *              See that the bottom of the GUI should show that john,
     *              joe, blow, clow, and drow are all working on this GUI.
     *      
     * Testing strategy for whiteboardopenerror:
     *      Receive message from the server with form:
     *          whiteboardopenerror A whiteboard with that name does not exist.
     *      Create a username and then pass whiteboardopenerror message to client and 
     *      ensure that an error message is loaded on the GUI.
     *      
     * Note that there are no test methods for this command.  All of the tests
     * for this command are manual. The user must be able to see the error
     * messages on the screen. Notice that this follows the testing strategy
     * described above.  
     *      Connect client to the server using the "username [username]"
     *      command on the server side.
     *          Type 'username blue'
     *      Type client.handleRequest("whiteboardopenerror A whiteboard with that name does not exist.")
     *              
     * Testing strategy for drawLine:
     *      Receive message from the server with form:
     *          drawLine [line]
     *      Create a username, create a board, open a board, and then pass the drawLine
     *      message to the client and ensure that a line is drawn on the WhiteBoard GUI.
     *      
     * Note that there are no test methods for this command.  All of the tests
     * for this command are manual. The user must be able to see the line drawn on the GUI. 
     * Notice that this follows the testing strategy described above.  
     *      Connect client to the server using the "username [username]" command on the 
     *      server side.
     *          Type 'username blue'
     *      Create board on the server using the "create [name]" command on the server
     *      side.
     *          Type 'create red'
     *      Open board on the server using the "open [username] [name]" command on the
     *      server side
     *          Type 'open blue red'
     *      Type client.handleRequest("drawLine" + [string rep of line])
     *          See that the line is drawn on the user's respective GUI.
     *          
     * Testing strategy for reset:
     *      Receive message from the server with form:
     *          reset
     *      Create a username, create a board, open a board, draw a line on the board, 
     *      then pass the reset message to the client and ensure that the GUI is 
     *      completely white.
     *      
     * Note that there are no test methods for this command.  All of the tests
     * for this command are manual. The user must be able to clear the canvas. 
     * Notice that this follows the testing strategy described above.  
     *      Connect client to the server using the "username [username]" command on the 
     *      server side.
     *          Type 'username blue'
     *      Create board on the server using the "create [name]" command on the server
     *      side.
     *          Type 'create red'
     *      Open board on the server using the "open [username] [name]" command on the
     *      server side
     *          Type 'open blue red'
     *      Draw line on the server using the "draw [name] [x1] [y1] [x2] [y2] [red] [green] [blue] [thickness]"
     *          Type 'draw red 0 0 1 1 0 0 255 1' on the "blue" client.
     *      Type client.handleRequest("reset")
     *          See that the entirety of the canvas is set to white.
     */
    

	
	@Test
	public void whiteboardClientTest() {
		assertTrue(true);
	}

}
