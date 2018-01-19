package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.SwingUtilities;

import whiteboardGUI.WhiteBoardGUI;


/**
 * WhiteboardClient represents the client.
 * 
 * Abstraction Function:
 * 		username        - the username that this client has chosen and that the server has accepted for this
 * 				         client
 * 		server          - the socket by which this client communicates with the server
 * 		clientUsername  - the GUI by which the client keeps on requesting usernames until the server accepts the
 * 						 desired username
 * 		clientInterface - the GUI by which the client makes requests for opening and creating whiteboards.
 * 		whiteboardGUI   - the GUI displaying the whiteboard that the client has requested
 * 		whiteboard      - the name of the whiteboard that the client currently has open
 * 
 * Representation Invariant:
 * 		The first thing that the client must do is keep on requesting usernames until the server accepts
 * 			their desired username.  The client cannot do anything until the server has accept the desired 
 * 			username.
 * 		The client can only have one whiteboard open at a time.
 * 		The name of the whiteboard that is open in whiteboardGUI is whiteboard.
 * 
 * Thread safety argument:
 * 		There is no data sharing between any of the threads.
 * 		Only one request is handled at a time.
 * 
 * 		Only one thread is made per connection with the synchronized method handleConnection. Opening and populating the 
 * 		client interface is done in a synchronized method and reads the mutable dataset of currently open whiteboards. 
 * 
 * 		All read and writes capabilities that exist are in openClientInterface and handleConnection. Both of these
 * 		methods are synchronized.
 * 
 * 		All other methods do not touch mutable data that has potential to not be thread safe.
 * 
 */
public class WhiteboardClient {
	private String username;
	private final Socket server;
	
	private final ClientUsername clientUsername;
	
	private boolean isClientInterfaceOpen;
	private ClientService clientInterface;
	
	private String whiteboard;
	private WhiteBoardGUI whiteboardGUI;
	private boolean hasWhiteboardOpen;
	
	private String[] usersOnLine;
	
   
	/**
	 * Creates a WhiteboardClient object
	 * 
	 * @param server the socket by which this WhiteboardClient object is connected to and 
	 * 				 can therefore communicate with the server
	 */
	public WhiteboardClient(Socket server) {
		this.username = null;
		this.server = server;
		this.isClientInterfaceOpen = false;
		this.usersOnLine = null;
		this.clientInterface = null;
		this.whiteboard = null;
		this.hasWhiteboardOpen = false;
		
		clientUsername = new ClientUsername(this);
		
		// Open a Client Username GUI to obtain the username of this WhiteboardClient
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				clientUsername.setVisible(true);
			}
		});
		
		// Start handling connections.
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				handleConnection();
			}
		});
		thread.start();
	}
	
	/**
	 * Returns the name of the whiteboard that is currently open for this client.
	 * 
	 * @return the name of the whiteboard that is currently open for this WhiteboardClient
	 */
	public String getWhiteboardName() {
		return whiteboard;
	}
	
	/**
	 * Returns the value of the username.
	 * 
	 * @return the username of this WhiteboardClient
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Sets the value of the username.
	 * 
	 * @param username the username of this WhiteboardClient
	 */
	protected void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Opens the client interface, which includes places to allow for the client to 
	 * request whiteboards to be opened and created.
	 * 
	 */
	protected synchronized void openClientInterface () {
		this.isClientInterfaceOpen = true;
		clientInterface = new ClientService(this);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				clientInterface.setVisible(true);
			}
		});
		
		// Request all of the whiteboards.
		sendMessage("list");
	}

	/**
	 * Communicates with the server, sending the specified message to the server that
	 * this client is connected to.
	 * 
	 * @param messageToServer the message to be sent to the server from this client
	 * @modifies send messageToServer to the server that this client is connected to
	 * @throws IOException
	 */
	public void sendMessage (final String messageToServer) {
		Thread thread = new Thread (new Runnable() {
			@Override
			public void run() {
				try {
		        	PrintWriter out = new PrintWriter(server.getOutputStream(), true);
		        	out.println(messageToServer);
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException ("The client can no longer connect to the server");
				}
			}
		});
		thread.start();
	}
	
	/**
     * Handle the server connection. Returns when this client disconnects.
     * 
	 */
	public synchronized void handleConnection () {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				// An empty string from the server indicates that the server has responded to one
				// of the client's requests but the client does not have to do anything in return.
				if (line != ""){ 
					handleRequest(line);
				}	
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException ("The client can no longer connect to the server.");
		}
	}
	
    /** Handler for server input, performing requested operations and returning an output message.
     * 
     * Valid server input is of the following form:
     * 		(1) "usernameerror That username is already being used." -
     * 				The server's indication that it has rejected the client's requested username
     * 		(2) "usernameerror There should be no spaces in the username." -
     * 				The server's indication that it has rejected the client's requested username
     * 		(3) "usernameCreated [username]" - 
     * 				The server's indication that it has accepted the client's requested username,
     * 				[username]
     * 		(4) "allUsersOnline [usernames]" -
     * 				The server's indication that another client has connected to the server and has
     * 				been assigned a valid username (i.e. unique and does not contain spaces) or that
     * 				a client has disconnected from the server.
     * 				[usernames] is a String containing the usernames of all of the clients connected
     * 				to the server, each separated by a space.
     * 		(5) "whiteboardnameerror Whiteboard names cannot contain any spaces." -
     * 				The server's indication that it has rejected the client's requested whiteboard name
     * 		(6) "whiteboardnameerror A whiteboard with that name has already created.  Please choose another name." -
     * 				The server's indication that it has rejected the client's requeste whiteboard name
     * 		(7) "whiteboardcreated [name]" -
     * 				The server's indication that it has accepted the client's requested whiteboard name,
     * 				[name]
     * 		(8) "allwhiteboards [whiteboards]" -
     * 				The server's indication that another client has created another whiteboard
     * 				[whiteboards] is a String containing the names of all the whiteboards currently saved
     * 				on the server, in the order that they were created, each separated by a space
     * 		(9) "open [whiteboardname] [whiteboard]" -
     * 				[whiteboardname] is the name of the whiteboard that this client requested to open.
     * 				[whiteboard] is the string representation of the whiteboard that this client requested
     * 				to open.
     * 		(10)"alsoediting [usernames]" -
     * 				The server's indication that another client has connected to the whiteboard that this client
     * 				currently has open.
     * 				[usernames] is a String containing the usernames of all of the client connected to the server, each
     * 				separated by a space.
     * 		(11)"whiteboardopenerror A whiteboard with that name does not exist." -
     * 				The server's indication that it has rejected the client's requested whiteboard name
     * 		(12)"drawLine [line]" -
     * 				The server's indication that the whiteboard that this client is connected to has changed.
     * 				[line] is a string representation of the line that has been added to the client.
     * 		(13)"reset" -
     * 				The server's indication that the whiteboard that this client is connected to has been reseted
     * 				to be completely white
     * 
     * @param the message from the server
     * 
     */
	public void handleRequest(String input) {
        String regex = "(usernameerror -?.+)|(usernameCreated -?.+)|(allUsersOnline -?.+)|"
                	 	+ "(whiteboardnameerror -?.+)|(whiteboardcreated -?.+)|(allwhiteboards -?.*)|"
                	 	+ "(open -?.+ -?.*)|(alsoediting -?.+)|(whiteboardopenerror -?.+)|"
                		+ "(drawLine (\\d+ \\d+ \\d+ \\d+ \\d+ )*)|(reset)";
        
        String[] tokens = input.split(" ");
        if (tokens[0].equals("usernameerror")) {
        	String errorMessage = getStringAfterSpace(input);
        	System.out.println("errorMessage");
        	clientUsername.updateErrorMessage(errorMessage);
        }
        else if (tokens[0].equals("usernameCreated")) {
        	String username = tokens[1];
        	this.username = username;
        	clientUsername.dispose();
        	openClientInterface();
        	
        	// If the command saying that "allUsersOnline" has already come back, then
        	if (usersOnLine != null) {
        		clientInterface.setUsersOnLine(usersOnLine);
        	}
        	// If the command has not come back then deal with it when it does.
        }
        else if (tokens[0].equals("allUsersOnline")) {
        	this.usersOnLine = getStringAfterSpace(input).trim().split(" ");
        	if (isClientInterfaceOpen) {
        		clientInterface.setUsersOnLine(usersOnLine);
        	}
        }
        else if(tokens[0].equals("whiteboardnameerror")) {
        	String errorMessage = getStringAfterSpace(input);
        	clientInterface.setErrorMessage(errorMessage);
        }
        else if(tokens[0].equals("whiteboardcreated")) {
        	// Does nothing
        }
        else if(tokens[0].equals("allwhiteboards")) {
        	if(tokens.length > 1) { // At least one whiteboard has been created.
            	String[] allWhiteboards = getStringAfterSpace(input).trim().split(" ");
            	clientInterface.setWhiteboardsCreated(allWhiteboards);
        	}
        }
        else if(tokens[0].equals("open")) {
        	whiteboard = tokens[1];
        	
        	String repOfWhiteboard = "";
        	if (tokens.length >= 3) {
        		repOfWhiteboard = getStringAfterSecondSpace(input).trim();
        	}
        	else {
        		repOfWhiteboard = "";
        	}
        	
        	whiteboardGUI = new WhiteBoardGUI(this, whiteboard, repOfWhiteboard);
        	whiteboardGUI.setVisible(true);
        	hasWhiteboardOpen = true;
        	sendMessage("getUsersOnWhiteboard " + whiteboard);
        }
        else if(tokens[0].equals("alsoediting")) {
        	if (whiteboardGUI != null) {
        		whiteboardGUI.setUsersOnline(getStringAfterSpace(input));
        	}
        }
        else if(tokens[0].equals("whiteboardopenerror")) {
        	String errorMessage = getStringAfterSpace(input);
        	clientInterface.setErrorMessage(errorMessage);
        }
        else if(tokens[0].equals("drawLine")) {
        	String line = getStringAfterSpace(input).trim();
        	
        	if (whiteboardGUI != null) { // If the user has not closed it in the mean time.
        		whiteboardGUI.drawLine(line);
        	}
        }
        else if(tokens[0].equals("reset")) {
        	if (whiteboardGUI != null) { // If the user has not closed it in the mean time.
        		whiteboardGUI.clearCanvas();
        	}
        }
	}
	
	/**
	 * Returns the part of the specified string after the first space in the string.
	 * 
	 * @param input must contain at least one space and must contain at least one character
	 * 				after its first space
	 * @return the part of input after the first space in input
	 */
	protected static String getStringAfterSpace(final String input) {
		int indexOfSpace = input.indexOf(' ');
		String stringAfterSpace = input.substring(indexOfSpace);
		return stringAfterSpace;
	}
	
	/**
	 * Returns the part of the specified string after the second space in the string.
	 * 
	 * @param input must contain at least two spaces and must contain at least one character
	 * 				after its second space
	 * @return the part of input after the second space in input
	 */
	protected static String getStringAfterSecondSpace(final String input) {
		int indexOfSpace = input.indexOf(' ');
		int indexOfSecondSpace = input.indexOf(' ', indexOfSpace + 1);
		String stringAfterSpace = input.substring(indexOfSecondSpace);
		return stringAfterSpace;
	}
	
	/**
	 * Indicates whether or not this WhiteboardClient already has a whiteboard open.
	 * 
	 * @return true only if this WhiteboardClient is already connected to a whiteboard, returns
	 * 		   false otherwise.
	 */
	public boolean hasWhiteboardOpen() {
		return hasWhiteboardOpen;
	}
	
	public static void main(final String[] args) {
		
		// First connect to the server.  Once a connection has been established, 
		// a WhiteboardClient object is created and the clientConnectGUI is closed.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ClientConnectGUI clientConnectGUI = new ClientConnectGUI();
				clientConnectGUI.setVisible(true);
			}
		});
	}
	
	/**
	 * Closes the whiteboardGUI.  Must be called when the whiteboardGUI is closed.
	 * 
	 */
	public void closeWhiteboardGUI() {
		whiteboardGUI = null;
		whiteboard = null;
		hasWhiteboardOpen = false;
	}
}
