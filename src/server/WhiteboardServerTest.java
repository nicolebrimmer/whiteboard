package server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import model.Whiteboard;
import org.junit.Test;

public class WhiteboardServerTest {
	private final static int port = 4444;
	private static AtomicInteger portIncrementer = new AtomicInteger(1);
	
	/**
	 * The following test methods test the create command in the 
	 * handleRequest method in the WhiteboardServer class.
	 * 
	 * The testing strategy for the create command in the handleRequest method:
	 * 		Enter the following command when a whiteboard named [name] has not been created 
	 * 		and when a whiteboard named [name] has already been created:
	 * 			create [name]
	 * 		For the above command, test whiteboard names that
	 * 			include a space (should return an error)
	 * 			include a digit
	 * 			include another non-letter character (such as !)
	 * 
	 * In addition, one must manually test the create command via telnet.  Notice that
	 * this follows the same testing strategy as outlined above.
	 * 		Connect multiple users to the server and create a unique username for each client
	 * 		using the "create [username]" command.
	 * 		Type "create he!3o" on one client.
	 * 			A message should be sent to all clients of the form: "allwhiteboards he!3o"
	 * 			A message should be sent back to the client that created the whiteboard of the
	 * 				form: "whiteboardcreated he!3o"
	 * 		Type "create bye" on another client.
	 * 			A message should be sent to all clients of the form: "allwhiteboards hello bye"
	 * 			A message should be sent back to the client that created the whiteboard of the
	 * 				form: "whiteboardcreated bye"
	 * 		Type "create hello" on a client.
	 * 			A message should be sent back to the client that attempted to create the whiteboard
	 * 				of the form: "whiteboarderror A whiteboard with that name has already created.  
	 * 				Please choose another name."
	 * 		Type "create hello hello" on a client.
	 * 			A message should be sent back to the client that attempted to create the whiteboard
	 * 				of the form: "whiteboarderror A whiteboard with that name has already created.  
	 * 				Please choose another name."
	 */
	
	// Creates a server.
	@Test
	public void whiteboardServerConstructorTest() {
		try {
			WhiteboardServer server = new WhiteboardServer(port + portIncrementer.getAndIncrement());
			assertTrue(server.checkRep());
		} catch (IOException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}
	
	// Creates a whiteboard whose name is [name] where there is no whiteboard
	// in the server whose name is [name].
	// The whiteboard name includes a digit and another non-letter character.
	@Test
	public void createWhiteboardNotInServerTest() {
		try {
			WhiteboardServer server = new WhiteboardServer(port + portIncrementer.getAndIncrement());
			assertTrue(server.checkRep());
			
			String name = "whiteboard1!", createRequest = "create " + name;
			String[] expectedNames = {name};
			
			// Create the whiteboard.
			String messageBack = server.handleRequest(createRequest, null);
			assertArrayEquals(expectedNames, server.getNames());
			assertTrue(server.checkRep());
			assertEquals("whiteboardcreated " + name, messageBack);
		} 
		catch (IOException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}
	
	// Creates a whiteboard whose name is [name] where there is no whiteboard
	// in the server whose name is [name].
	// The whiteboard name includes a space.
	@Test
	public void createWhiteboardWithSpaceTest() {
		try {
			WhiteboardServer server = new WhiteboardServer(port + portIncrementer.getAndIncrement());
			assertTrue(server.checkRep());
			
			String name = "whiteboard 1!", createRequest = "create " + name;
			String[] expectedNames = {};
			
			// Create the whiteboard.
			String messageBack = server.handleRequest(createRequest, null);
			assertArrayEquals(expectedNames, server.getNames());
			assertTrue(server.checkRep());
			assertEquals(messageBack, WhiteboardServer.NO_SPACES_IN_WHITEBOARD);
		} 
		catch (IOException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}
	
	// Attempts to create a whiteboard whose name is [name] where there is a whiteboard
	// in the server already whose name is [name].
	@Test
	public void createWhiteboardAlreadyInServerTest() {
		try {
			WhiteboardServer server = new WhiteboardServer(port + portIncrementer.getAndIncrement());
			assertTrue(server.checkRep());
			
			String name = "whiteboard1", createRequest = "create " + name;
			String[] expectedNames = {name};
			
			// Create the whiteboard.
			server.handleRequest(createRequest, null);
			
			// Attempt to create the whiteboard again with the same name.
			String messageBack = server.handleRequest(createRequest, null);
			assertArrayEquals(expectedNames, server.getNames());
			assertTrue(server.checkRep());
			assertEquals(messageBack, WhiteboardServer.WHITEBOARD_ALREADY_CREATED);
		}
		catch (IOException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}
	
	/**
	 * The following test methods test the getAllWhiteboards method in the WhiteboardServer class.
	 * 
	 * The testing strategy for the getAllWhiteboards method:
	 * 		No whiteboards have been added.
	 * 		One whiteboard has been added.
	 * 		Three whiteboards have been added.
	 * 
	 * Note that this testing method could be further automated.  However, any attempt at automation
	 * would ressemble the method that it is testing very strongly and therefore would not be very useful.
	 */

	@Test
	public void getAllWhiteboardsTest() {
		try {
			WhiteboardServer server = new WhiteboardServer(port + portIncrementer.getAndIncrement());
			assertTrue(server.checkRep());
			
			String expectedWhiteboards = "";
			
			// No whiteboards have been created.
			assertEquals(expectedWhiteboards, server.getAllWhiteboards());
			
			// Create one whiteboard.
			String name1 = "whiteboard1";
			server.handleRequest("create " + name1, null);
			expectedWhiteboards += name1;
			assertEquals(expectedWhiteboards, server.getAllWhiteboards());
			
			// Create another whiteboard.
			String name2 = "SecondOne";
			server.handleRequest("create " + name2, null);
			expectedWhiteboards += " " + name2;
			assertEquals(expectedWhiteboards, server.getAllWhiteboards());
			
			// Create a third whiteboard.
			String name3 = "ThirdOne";
			server.handleRequest("create " + name3, null);
			expectedWhiteboards += " " + name3;
			assertEquals(expectedWhiteboards, server.getAllWhiteboards());
		}
		catch (IOException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}
	
	/**
	 * The following test methods test the open command in the 
	 * handleRequest method in the WhiteboardServer class.
	 * 
	 * The testing strategy for the open command in the handleRequest method:
	 * 		Enter the following command when a whiteboard named [name] has not been created 
	 * 		and when a whiteboard named [name] has already been created:
	 * 			open [username] [name]
	 * 		For the above command, test whiteboard names that
	 * 			include a space (should return an error)
	 * 			are valid.
	 * 
	 * In addition, one must manually test the username command via telnet.  Notice that
	 * this follows the same testing strategy as outlined above.
	 * 		Connect multiple users to the server using the "create [username]" command.
	 * 			One client should be called "leftWindow"
	 * 			The other client should be called "rightWindow"
	 * 		Type "create window1" on one client.
	 * 		Type "open leftWindow window1" on the "leftWindow" client.
	 * 			A message should be sent to the "leftWindow" client of the form 
	 * 			"open window1 "
	 * 			Another message should be sent to the "leftWindow" client of the form
	 * 			"alsoediting leftWindow"
	 * 		Type "open rightWindow window1" on the "rightWindow" client.
	 * 			A message should be sent to the "leftWindow" client of the form
	 * 			"open window1 "
	 * 			A message should be sent to both the "leftWindow" client and the "rightWindow"
	 * 			client of the form "alsoediting rightWindow"
	 * 
	 */
	
	// Attempts to open a whiteboard that is not already in the server.
	@Test
	public void openWhiteboardNotInServerTest() {
		try {
			WhiteboardServer server = new WhiteboardServer(port + portIncrementer.getAndIncrement());
			assertTrue(server.checkRep());
			
			String username = "username", name = "whiteboard1@", openRequest = "open " + username + " " + name;
			
			String messageBack = server.handleRequest(openRequest, null);
			assertEquals(messageBack, WhiteboardServer.NOT_CREATED_ERROR);
		}
		catch (IOException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}

	// Attempts to open a whiteboard whose name has a space that is not already in the server.
	@Test
	public void openWhiteboardWithSpaceTest() {
		try {
			WhiteboardServer server = new WhiteboardServer(port + portIncrementer.getAndIncrement());
			assertTrue(server.checkRep());
			
			String username = "username", name = "whiteboard1 @", openRequest = "open " + username + " " + name;
			
			String messageBack = server.handleRequest(openRequest, null);
			assertEquals(messageBack, WhiteboardServer.NOT_CREATED_ERROR);
		}
		catch (IOException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}
	
	// Opens a whiteboard that is already in the server.
	@Test
	public void openWhiteboardAlreadyInServerTest() {
		try {
			WhiteboardServer server = new WhiteboardServer(port + portIncrementer.getAndIncrement());
			assertTrue(server.checkRep());
			
			String username = "username", name = "whiteboard1!", createRequest = "create " + name;
			String[] openRequest = {"open ", username, name};
			
			// Create the whiteboard.
			server.handleRequest(createRequest, null);
			
			// Opens the whiteboard with same name.
			String messageBack = server.openWhiteboard(openRequest, false);
			assertTrue(server.checkRep());
			assertEquals(messageBack, "open " + name + " " + new Whiteboard(name, WhiteboardServer.WIDTH_OF_WHITEBOARDS, WhiteboardServer.HEIGHT_OF_WHITEBOARDS).toString());
		}
		catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	/**
	 * The following test methods test the create username command in the 
	 * handleRequest method in the WhiteboardServer class.
	 * 
	 * The testing strategy for the open command in the handleRequest method:
	 * 		Enter the following command when a client named [username] has not been created 
	 * 		and when a client named [username] has already been created:
	 * 			create [username]
	 * 		For the above command, test whiteboard names that
	 * 			include a space (should return an error)
	 * 			are valid.
	 * 
	 * In addition, one must manually test the username command via telnet.  Notice that
	 * this follows the same testing strategy as outlined above.
	 * 		Connect multiple users to the server.
	 * 		Type "username hello" on one client.
	 * 			A message should be sent to all clients of the form: "allUsersOnline hello"
	 * 			A message should be sent back to the client that created the username of the
	 * 			form "usernameCreated hello"
	 * 		Type "username hello" on a different client.
	 * 			A message should be sent back to the client that attempted to create the same username
	 * 				twice of the form "usernameerror That username is already being used."
	 * 		Type "username hello hello" on one client.
	 * 			A message should be sent back to the client that attempted to create a username with a
	 * 				space of the form "usernameerror There should be no spaces in the username."
	 * 		Type "username bl!5" on one client.
	 * 			A message should be sent to all clients of the form: "allUsersOnline hello bl!5"
	 * 			A message should be sent back to the client that created the username of the
	 * 			form "usernameCreated bl!5"
	 */
	
	// Attempts to create a username that is not already associated with the server.
	@Test
	public void createUniqueUsernameTest() {
		try {
			WhiteboardServer server = new WhiteboardServer(port + portIncrementer.getAndIncrement());
			assertTrue(server.checkRep());
			
			String username = "hello";
			String[] createRequest = {"username", username};
			String messageBack = server.createUsername(createRequest, null, false);
			assertEquals("usernameCreated " + username, messageBack);
		}
		catch (IOException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}
	
	// Attempts to create a username that is already associated with the server.
	@Test
	public void createSameUsernameTwiceTest() {
		try {
			WhiteboardServer server = new WhiteboardServer(port + portIncrementer.getAndIncrement());
			assertTrue(server.checkRep());
			
			// Creates the username once.
			String username = "hello";
			String[] createRequest = {"username", username};
			String messageBack = server.createUsername(createRequest, null, false);
			assertEquals(messageBack, "usernameCreated " + username);
			
			String messageBack2 = server.createUsername(createRequest, null, false);
			assertEquals(WhiteboardServer.USERNAME_ALREADY_CREATED, messageBack2);
		}
		catch (IOException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}
	
	// Attempts to create a username that contains a space.
	@Test
	public void createUsernameWithSpaceTest() {
		try {
			WhiteboardServer server = new WhiteboardServer(port + portIncrementer.getAndIncrement());
			assertTrue(server.checkRep());
			
			String[] createRequest = {"username", "hello", "there"};
			String messageBack = server.createUsername(createRequest, null, false);
			assertEquals(messageBack, WhiteboardServer.USERNAME_CONTAINS_SPACE);
		}
		catch (IOException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}
	
	/**
	 * The following test methods test the logout username command in the 
	 * handleRequest method in the WhiteboardServer class.
	 * 
	 * The testing strategy for the open command in the handleRequest method:
	 * 		Enter the following command:
	 * 			logout [username]
	 * 		For the above command, test when...
	 * 			the client whose username is [username] is also connected to a 
	 * 				whiteboard [name], which at least one other client is connected to.
	 * 			there is at least one client on the server that is not connected to the same whiteboard
	 * 				that the client whose username is [username] is connected to.
	 * 			there are no clients on the server that are also connected to the same whiteboard
	 * 		Do two logouts in a row, both of which are clients which were connected to a whiteboard.
	 * 
	 * Note that the test method basically only tests that you *can* logout.  The rest of the testing strategy
	 * is carried out through manual testing through telnet.
	 * 
	 * In addition, one must manually test the username command via telnet.  Notice that
	 * this follows the same testing strategy as outlined above.
	 * 		Connect multiple users to the server using the "create [username]" command.
	 * 			One client should be called "leftWindow"
	 * 			The other client should be called "rightWindow"
	 * 			Another client should be called "middleWindow"
	 * 		Type "create window1" on one client.
	 * 		Type "open leftWindow window1" on the "leftWindow" client.
	 * 		Type "open rightWindow window1" on the "rightWindow" client.
	 * 		Type "logout rightWindow" on the "rightWindow" client.
	 * 			The connection with the client "rigthWindow" should be terminated.
	 * 			A message should be sent to the clients "middleWindow" and "leftWindow" of the form
	 * 				"allUsersOnline middleWindow leftWindow"
	 * 			A message should be sent to the client "leftWindow" of the form "alsoediting leftWindow"
	 * 		Type "create window2" on one client.
	 * 		Type "open middleWindow window2" on the "middleWindow" client.
	 * 		Type "logout middleWindow" on the "middleWindow" client.
	 * 			The conenction the client "middleWindow" should be terminated.
	 * 			A message should be sent to the client "leftWindow" of the form "allUsersOnline leftWindow"
	 * 
	 */
	
	// Attempts to logout of the server.
	@Test
	public void logoutTest() {
		try {
			WhiteboardServer server = new WhiteboardServer(port + portIncrementer.getAndIncrement());
			assertTrue(server.checkRep());
			
			// Create the username.
			String username = "hello";
			String[] createRequest = {"username", username};
			String messageBack = server.createUsername(createRequest, null, false);
			assertEquals("usernameCreated " + username, messageBack);
			
			String[] logoutRequest = {"logout", username};
			String messageBack2 = server.logout(logoutRequest, false);
			assertTrue(server.checkRep());
			assertEquals("logout", messageBack2);
		}
		catch (IOException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}
	
	/**
	 * The following tests test the close username name command in the 
	 * handleRequest method in the WhiteboardServer class.
	 * 
	 * The testing strategy for the close command in the handleRequest method:
	 * 		Enter the following command:
	 * 			close [username] [name]
	 * 		For the above command, test when the whiteboard whose name is [name]
	 * 			is only open on the client whose username is [username]
	 * 			is open one at least one other client other than the client whose username is [username]
	 * 		Do two closes in a row, both of which are clients which were connected to the same whiteboard.
	 * 		Do two closes in a row, both of which are clients that are connected to different whiteboards.
	 * 
	 * 
	 * In addition, one must manually test the username command via telnet.  Notice that
	 * this follows the same testing strategy as outlined above.
	 * 		Connect multiple users to the server using the "create [username]" command.
	 * 			One client should be called "leftWindow"
	 * 			The other client should be called "rightWindow"
	 * 			Another client should be called "middleWindow"
	 * 		Type "create window1" on one client.
	 * 		Type "open leftWindow window1" on the "leftWindow" client.
	 * 		Type "open rightWindow window1" on the "rightWindow" client.
	 * 		Type "close rightWindow window1" on the "rightWindow" client.
	 * 			A message should be sent to the client "leftWindow" of the form "alsoediting leftWindow"
	 * 		Type "close leftWindow window2" on the "leftWindow" client.
	 * 		Type "create window2" on one client.
	 * 		Type "open middleWindow window2" on the "middleWindow" client.
	 * 		Type "close middleWindow" on the "middleWindow" client.
	 * 
	 * Note that there are no test methods for this command.  All of the tests for this command are manual.
	 * 
	 */
	
	/**
	 * The following tests test the draw command in the 
	 * handleRequest method in the WhiteboardServer class.
	 * 
	 * The testing strategy for the close command in the handleRequest method:
	 * 		Enter the following command:
	 * 			draw [name] [x1] [y1] [x2] [y2] [red] [green] [blue] [thickness]
	 * 		For the above command, test when the whiteboard whose name is [name]
	 * 			is only open on the client who made this request
	 * 			is open one at least one other client other than the client who made this request
	 * 			is subsequently opened by another client
	 * 		Do two draws in a row, on the same whiteboard that overlap.
	 * 
	 * 
	 * In addition, one must manually test the close command via telnet.  Notice that
	 * this follows the same testing strategy as outlined above.
	 * 		Connect multiple users to the server using the "create [username]" command.
	 * 			One client should be called "leftWindow"
	 * 			The other client should be called "rightWindow"
	 * 			Another client should be called "middleWindow"
	 * 		Type "create window1" on one client.
	 * 		Type "open leftWindow window1" on the "leftWindow" client.
	 * 		Type "open rightWindow window1" on the "rightWindow" client.
	 * 		Type "draw window1 0 0 1 1 0 0 255 1" on the "leftWindow" client.
	 * 			A message should be sent to both clients "leftWindow" and "rightWindow"
	 * 				of the form "drawLine 0 0 0 0 255 1 1 0 0 255"
	 * 		Type "open middleWindow window1" on the "middleWindow" client.
	 * 			A message should be sent to the "middleWindow" client of the form 
	 * 				"window1 0 0 0 0 255 1 1 0 0 255"
	 * 		Type "draw window1 1 1 2 2 0 255 0 1" on the "leftWindow" client.
	 * 			A message should be sent to both clients "leftWindow" and "rightWindow"
	 * 				of the form "drawLine 1 1 0 255 0 1 2 2 0 255 0 1""
	 * 		Type "open leftWindow window1" on the "leftWindow" client.
	 * 			A message should be sent to the client "leftWindow"
	 * 				of the form "window1 0 0 0 0 255 1 1 0 0 255 2 2 0 0 255"
	 * 
	 * Note that there are no test methods for this command.  All of the tests for this command are manual.
	 * 
	 */
	
	/**
	 * The following tests test the list command in the handleRequest method in the
	 * WhiteboardServer.
	 * 
	 * The testing strategy for the list command in the handleRequest method:
	 * 		Enter the following command:
	 * 			list
	 * 		For the above command, test when
	 * 			There are no whiteboards on the server
	 * 			There are at least two whiteboards on the server
	 * 		
	 * One must manually test the list command via telnet.  Notice that
	 * this follows the same testing strategy as outlined above.
	 * 		Connect multiple users to the server using the "create [username]" command.
	 * 			One client should be called "leftWindow"
	 * 			The other client should be called "rightWindow"
	 * 			Another client should be called "middleWindow"
	 * 		Type "list" in the "rightWindow" client.
	 * 			A message should be sent back to the "rightWindow" client of the form "allwhiteboards "
	 * 		Type "create window1" in the "middleWindow" client.
	 * 		Type "list" in the "leftWindow" client.
	 * 			A message should be sent back to the "leftWindow" client of the form "allwhiteboards window1"
	 * 		Type "create window2" in the "middleWindow" client.
	 * 		Type "list" in the "leftWindow" client.
	 * 			A message should be sent back to the "leftWindow" client of the form "allwhiteboards window1 window2"
	 * 
	 */ 
	
	/**
	 * The following tests the the getUsersOnWhiteboard [whiteboard]
	 * command in the handleRequest method in the WhiteboardServer.
	 * 
	 * The testing strategy for the list command in the handleRequest method:
	 * 		Enter the following command:
	 * 			getUsersOnWhiteboard [whiteboard]
	 * 		For the above command, test when
	 * 			There are no users connected to the whiteboard whose name is [whiteboard]
	 * 			There are at least two users connected to the whiteboard whose name is [whiteboard]
	 * 		
	 * One must manually test the getUsersOnWhiteboard command via telnet.  Notice that
	 * this follows the same testing strategy as outlined above.
	 * 		Connect multiple users to the server using the "create [username]" command.
	 * 			One client should be called "leftWindow"
	 * 			The other client should be called "rightWindow"
	 * 			Another client should be called "middleWindow"
	 * 		Type "create window1" in the "leftWindow" client.
	 * 		Type "getUsersOnWhiteboard window1" in the "leftWindow" client.
	 * 			A message should be sent back to the "leftWindow" client of the form
	 * 			"alsoediting "
	 * 		Type "open window1" in the "leftWindow" client.
	 * 		Type "open window1" in the "rightWindow" client.
	 * 		Type "getsUsersOnWhiteboard window1" in the leftWindow" client.
	 * 			A message should be sent back to the "leftWindow" client of the form
	 * 			"also editing leftWindow, rightWindow"
	 * 		
	 */
	
	/**
	 * The following tests test the reset [whiteboard] command in the handleRequest
	 * method in the WhiteboardServer.
	 * 
	 * The testing strategy for the reset command in the handleRequest method:
	 * 		Enter the following command:
	 * 			reset [whiteboard]
	 * 		For the above command, test when
	 * 			There are at least two users connected to the whiteboard whose name is [whiteboard]
	 * 			and at least one user who is not.
	 * 
	 * One must manually test the reset command via telnet.  Notice that
	 * this follows the same testing strategy as outlined above.
	 * 		Connect multiple users to the server using the "create [username]" command.
	 * 			One client should be called "leftWindow"
	 * 			The other client should be called "rightWindow"
	 * 			Another client should be called "middleWindow"
	 * 		Type "create window1" in the "leftWindow" client.
	 * 		Type "open window1" in the "leftWindow" client.
	 * 		Type "open window1" in the "rightWindow" client.
	 * 		Type "reset window1" in the "leftWindow" client.
	 * 			A message should be sent back to both the "rightWindow" client and the "leftWindow" client
	 * 			of the form "reset"
	 * 
	 */
	
	// Attempts to enter an invalid command.
	@Test
	public void invalidCommandTest() {
		try {
			WhiteboardServer server = new WhiteboardServer(port + portIncrementer.getAndIncrement());
			assertTrue(server.checkRep());
			
			String messageBack = server.handleRequest("afsdjk", null);
			assertTrue(server.checkRep());
			assertEquals(messageBack, WhiteboardServer.INVALID_INPUT_ERROR);
		}
		catch (IOException e) {
			assertTrue(false);
			e.printStackTrace();
		}
	}
}
