package server;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import model.Whiteboard;


/**
 * WhiteboardServer is a datatype that represents the server 
 * 
 * Abstraction Function:
 * 		ServerSocket: the socket to which the server is listening for client
 * 					  conenctions
 * 		whiteboards : the whiteboards that have been created during a server
 * 					  session
 * 		names       : the names of the whiteboards that have been created
 * 					  during a server session
 * 		clients     : the keys are the usernames of the clients that are connected to this 
 * 					  WhiteboardServer and the values are the sockets via which each client is 
 * 					  connected to the server
 * 
 * Representation Invariant:
 * 		The name of the ith Whiteboard in whiteboards is the ith entry in name.
 * 		Every Whiteboard that has been created while the server is running is 
 * 		in whiteboards.
 * 		The ith Whiteboard in whiteboards is the ith Whiteboard object created.
 * 		The names of whiteboards cannot contain spaces.
 * 		Each client must have a different username and usernames cannot contain spaces.
 * 		The first set of messages from the client to the server must be requests for a username.
 * 		The client must keep on requesting a username until the username that the client requests is
 * 		accepted by the server (i.e. is unique and doesn't contain any spaces).
 * 		Each client of the server must be represented in clients.
 * 
 * Threadsafety argument:
 * The threads in the system are:
 * 		- main thread accepting new connections
 * 		- one thread per connected client, handling just that client
 * 
 * The serverSocket object is confined to the main thread.
 * 
 * The Socket object for a client is confined to that client's thread;
 * the main thread loses its reference to the object right after starting the client thread.
 * 
 * All read and writes to the mutable representation of the WhiteboardServer (whiteboard, names, and clients)
 * are synchronized on this WhiteboardServer so that only one thread can change or read these objects at any given
 * time.
 * 
 * The WhiteboardServer does not share any memory with its clients.
 *
 */
public class WhiteboardServer {
    private final ServerSocket serverSocket;
    private final ArrayList<Whiteboard> whiteboards;
    private final ArrayList<String> names;
    private final HashMap<String, Socket> clients;
    
    public static final int port = 4444;
    
    protected static final String NO_SPACES_IN_WHITEBOARD = "whiteboardnameerror Whiteboard names cannot contain any spaces.";
    protected static final String WHITEBOARD_ALREADY_CREATED = "whiteboardnameerror A whiteboard with that name has already created.  Please choose another name.";
    public static final String INVALID_INPUT_ERROR = "Invalid input.";
    protected static final String NO_WHITEBOARDS_MESSAGE = "";
    protected static final String LOGOUT_REQUEST = "logout";
    protected static final String NOT_CREATED_ERROR = "whiteboardopenerror A whiteboard with that name has not been created.";
    protected static final String USERNAME_ALREADY_CREATED = "usernameerror That username is already being used.";
    protected static final String USERNAME_CONTAINS_SPACE = "usernameerror There should be no spaces in the username.";
    
    protected static final int WIDTH_OF_WHITEBOARDS = 800;
    protected static final int HEIGHT_OF_WHITEBOARDS = 600;
	
    /**
     * Make a WhiteboardServer that listens for connections on port.
     * 
     * @param port port number, requires 0 <= port <= 65535
     */
    public WhiteboardServer(final int port) throws IOException {
        serverSocket = new ServerSocket(port);
        whiteboards = new ArrayList<Whiteboard>();
        names = new ArrayList<String>();
        clients = new HashMap<String, Socket>();
    }
    
    /**
     * Run the server, listening for client connections and handling them.
     * Never returns unless an exception is thrown.
     * 
     * @throws IOException if the main server socket is broken
     *                     (IOExceptions from individual clients do *not* terminate serve())
     */
    public void serve() throws IOException {
        while (true) {
            // block until a client connects
            final Socket socket = serverSocket.accept();
            
            // start a new thread to handle the client connection
            Thread thread = new Thread (new Runnable() {
            	public void run() {
            		// the client socket object is now owned by this thread,
            		// and mustn't be touched again in the main thread
            		
                    // handle the client
                    try {
                    	handleConnection(socket);
                    } catch (IOException e) {
                        e.printStackTrace(); // but don't terminate serve()
                    }
            	}
            });

            thread.start();
        }
    }
    
    /**
     * Handle a single client connection. Returns when client disconnects.
     * 
     * @param socket socket where the client is connected
     * @throws IOException if connection has an error or terminates unexpectedly
     */
    private void handleConnection(Socket socket) throws IOException {
    	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        try {
            for (String line = in.readLine(); line != null; line = in.readLine()) {
            	// An empty string indicates to that the client has done something in response to 
            	// the server's message but the server does not have to do anything in response.
            	if (line != "") {
            		String reply = handleRequest(line, socket);
                	
                	if(reply == LOGOUT_REQUEST) {
                		break;
                	}
                	else {
                		out.println(reply);
                	}
            	}
            }
        }
        catch (IOException e) {
        	e.printStackTrace();
        }
        finally {
            out.close();
            in.close();
        }
    }
    
    /**
     * Checks the representation invariant of the WhiteboardServer.
     * 
     * @return true only if for every integer i in the range [0, whiteboards.size())
     * 		   the name of Whiteboard object stored at whiteboards[i] is names[i]
     */
    public synchronized boolean checkRep() {
    	boolean flag = true;
    	
    	for (int i = 0; i < whiteboards.size(); i++) {
    		flag = flag && names.get(i).equals(whiteboards.get(i).getName());
    	}
    	
    	return flag;
    }
    
    /**
     * Returns the array representation of the names of the whiteboards that have
     * been created during this session of the server.
     * 
     * @return an array containing the names of all of the Whiteboard objects that have
     * 		   been created during this session of the server
     * 		   the ith String in the returned array is the name of the ith Whiteboard object
     * 		   that has been created.
     */
    protected synchronized String[] getNames() {
    	String[] result = new String[names.size()];
    	return names.toArray(result);
    }
    
    /**
     * Handler for client input, performing requested operations and returning an output message.
     * 
     * Valid client input is of the following form.
     * 		(1) create [name] - 
     * 				[name] cannot be an empty string
     * 				If [name] contains any spaces,
     * 					this method returns an output message of the form "whiteboardnameerror Whiteboard names cannot 
     * 					contain any spaces."
     * 				If a whiteboard whose name is [name] is saved on the server (i.e. has already been
     * 				created), 
     * 					this method returns an output message of the form "whiteboardnameerror A whiteboard with that name has 
     * 					already created.  Please choose another name."
     * 				Otherwise, if a whiteboard whose name is [name] is not saved on the server (i.e. has never been created) 
     * 				and [name] does not contain any spaces, 
     * 					this method creates a whiteboard whose name is [name] and returns a message of the form "whiteboardcreated [name]"
     * 					this method also notifies all other clients of this change with a message of the form 
     * 					"allwhiteboards [whiteboards]" where [whiteboards] is a String containing the names of all the whiteboards currently saved
     * 					on the server, in the order that they were created, each separated by a space
     * 		(2) open [username] [name] - 
     * 				[username] must represent the client that is connecting through the currently handled port
     * 				If a whiteboard whose name is [name] is saved on the server,
     * 					this method returns "[name] [whiteboard]" where [whiteboard] is a string representation of 
     * 					the whiteboard to the client.  
     * 					this method also adds the client to the specified whiteboard and notifies all clients currently connected 
     * 					to the specified whiteboard of the new client with a message of the form "alsoediting [usernames]" where 
     * 					[usernames] is a string containing the usernames of all of the clients connected to the whiteboard named [name]
     * 				Otherwise, 
     * 					then returns an output message of the form "whiteboardopenerror A whiteboard with that name does not exist."
     * 		(3) logout [username] - 
     * 				[username] must represent the client that is connecting through the currently handled port.
     * 				This method returns a message of the form "logout" and removes the client from all whiteboards
     * 				that it is connected to and notifies all other clients both of the fact that they have 
     * 				disconnected from the whole server (with a message of the form "allUsersOnline [usernames] where [usernames] is 
     * 				a String containing the usernames of all of the client currently connected to the server")
     * 				and that they have disconnected from all of the whiteboards (with a message of the form "alsoediting [usernames]"
     * 				where [usernames] is a string containing the usernames of all of the client connected to the whiteboard that the client
     * 				whose username was [username] was connected to.
     * 		(4) username [username] - 
     * 				[username] cannot be an empty string
     * 				If a client whose username is [username] is connected to the server,
     * 					this method returns "usernameerror That username is already being used."
     * 				If [username] contains a space,
     * 					this method returns "usernnameerror There should be no spaces in the username."
     * 				Otherwise,
     * 					this method creates an entry in the HashMap corresponding to this username and client and 
     * 					returns "usernameCreated username" and sends a message to all of the clients of the form 
     * 					"allUsersOnline [usernames]" where [usernames] is a string containing the usernames of all of 
     * 					clients connected to the server in the order that they were assigned usernames, each separated by
     * 					a space
     * 		(5) close [username] [name] -
     * 				[username] must a client on the server that is connected to the whiteboard whose name is [name]
     * 				This method returns an empty string.
     * 				This method removes the client whose username is [username] from whiteboard whose name is [name] and 
     * 					notifies all clients currently connected to the specified whiteboard of this disconnection 
     * 					with a message of the form "alsoediting [usernames]" where [usernames] is a string containing 
     * 					the usernames of all of the clients connected to the whiteboard named [name]
     * 		(6) draw [name] [x1] [y1] [x2] [y2] [red] [green] [blue] [thickness] -
     * 				[name] must be a whiteboard on the server
     * 				This method requests a line to be draw on the whiteboard named [name] from ([x1], [y1]) to 
     * 				([x2], [y2]) with color [red] [green] [blue] and thickness [thickness]
     * 				Note that ([x1], [y1]) and ([x2], [y2]) must be points on the whiteboard named [name]
     * 				This method returns an empty string.
     * 				This method also sends out a message to all of the clients connected to the whiteboard [name]
     * 				that a line has been added to the whiteboard via a message of the form "drawLine [line]" where [line]
     * 				is the string representation of the line that has been added to the whiteboard whose name is [name].
     * 		(7) list - 
     *				This method returns "allwhiteboards [whiteboards]" where [whiteboards] is a String containing 
     *				the names of all the whiteboards currently saved on the server, in the order that they 
     *				were created, each separated by a space
     *		(8) getUsersOnWhiteboard [whiteboard] -
     *				This method returns "alsoediting [usernames]" where [usernames] is a String containing the usernames
     *				of all clients currently editing the whiteboard whose name is [whiteboard].
     *				[whiteboard] must be the name of a whiteboard that is saved on the server
     *		(9) reset [whiteboard] -
     *				[whiteboard] must be the name of a whiteboard that is saved on the server and sends
     *				a message of the form "reset" to all of the clients connected to the whiteboard [whiteboard]
     *				This method clears the whiteboard whose name is [whiteboard].
     * 
     * If the client input is not valid, then this method returns an output message of the form "Invalid input."
     * 
     * @param input message from client
     * @param socket the socket via which the client is connected to the server
     * @return message to client
     */
    protected String handleRequest(final String input, final Socket socket) {
        String regex = "(create -?.+)|(open -?.+ -?.+)|(draw -?.+ -?\\d+ -?\\d+ -?\\d+ -?\\d+ -?\\d+ -?\\d+ -?\\d+ -?\\d+)|"
                + "(username -?.+)|(logout -?.+)|(close -?.+ -?.+)|(list)|(getUsersOnWhiteboard -?.+)|(reset -?.+)";
        
        if ( ! input.matches(regex)) {
            return INVALID_INPUT_ERROR;
        }
        String[] tokens = input.split(" ");
        if (tokens[0].equals("create")) {
        	return createWhiteboard(tokens);
        } 
        else if (tokens[0].equals("username")) {
        	return createUsername(tokens, socket, true);
        }
        else if (tokens[0].equals("open")) {
        	return openWhiteboard(tokens, true);
        }
        else if (tokens[0].equals("logout")) {
            return logout(tokens, true);
        }
        else if (tokens[0].equals("close")) {
        	return closeWhiteboard(tokens);
        }
        else if (tokens[0].equals("draw")) {
        	return drawWhiteboard(tokens);
        }
        else if(tokens[0].equals("list")) {
        	return "allwhiteboards " + getAllWhiteboards();
        }
        else if(tokens[0].equals("getUsersOnWhiteboard")) {
        	return getUsernamesOnWhiteboard(tokens);
        }
        else if(tokens[0].equals("reset")) {
        	return resetWhiteboard(tokens);
        }
        // Should never get here--make sure to return in each of the valid cases above.
        throw new UnsupportedOperationException();
    }
    
    /**
     * Sends out a message to all of the clients of this WhiteboardServer that have the whiteboard named [name]
     * open
     * 
     * @param name the name of the whiteboard.  There must be a whiteboard named [name] saved on the 
     * 		       server
     * @param message the message to be sent to all of the clients of this WhiteboardServer that have the whiteboard named
     * 				  [name] open
     * @modifies sends the message to all of the clients of this WhiteboardServer that have the whiteboard named
     * 			 [name] open
     */
    public synchronized void sendMessageToSomeClients (final String name, final String message) {
    	// Get the usernames of all of the clients with that whiteboard open
    	int index = names.indexOf(name);
    	
    	// If some clients have the specified board open,...
    	if(index != -1) {
    		Whiteboard whiteboard = whiteboards.get(index);
        	String[] usernames = whiteboard.getUsernames().split(" ");
        	
        	// Send the message to each client in usernames
        	for (int i = 0; i < usernames.length; i++) {
        		Socket socket = clients.get(usernames[i]);
        		if (socket != null) {
        			try {
        				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        				out.println(message);
        			} 
                	catch (IOException e) {
        				e.printStackTrace();
        			}
        		}
        	}
    	}
    	
    }
    
    /**
     * Sends out a message to all of the clients of this WhiteboardServer.
     * 
     * @param message the message to be sent to all of the clients of this WhiteboardServer
     * @modifies sends the message message to all of the clients of this WhiteboardServer
     */
    public synchronized void sendMessageToAllClients (final String message) {
    	Set<String> usernames = clients.keySet();
    	
    	for (String username: usernames) {
    		Socket socket = clients.get(username);
        	try {
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				out.println(message);
			} 
        	catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
    
    /**
     * Attempts to create a whiteboard with the specified name, according to the specification
     * of the 'create [name]' command in the handleRequest method's specifications.
     * 
     * @param tokens cannot contain any empty strings, must be of length 2 or more and must have
     * 				 the following form:
     * 					create [name 1] [name 2] [name 3] .... [name n]
     * 				 where [name 1] [name 2] [name 2] [name 3] .... [name n] is the requested whiteboard name
     * @return message to client
     */
    private synchronized String createWhiteboard(final String [] tokens) {
		// If a whiteboard with the specified name has already been added.
		if (tokens.length != 2) {
			return NO_SPACES_IN_WHITEBOARD;
		}
		
		else {
			String name = tokens[1];
			
			if (names.indexOf(name) != -1) {
				return WHITEBOARD_ALREADY_CREATED;
			}
			else {
				Whiteboard whiteboard = new Whiteboard(name, WIDTH_OF_WHITEBOARDS, HEIGHT_OF_WHITEBOARDS);
				whiteboards.add(whiteboard);
	    		names.add(whiteboard.getName());
	    		
	    		// Notify all clients of this new whiteboard.
	    		sendMessageToAllClients("allwhiteboards " + getAllWhiteboards());
	    		
	    		return "whiteboardcreated " + name;
	    	}
		}
    }
    
    /**
     * Returns a string containing the names of all of the whiteboards saved on the server, each one separated
     * by a space.
     * 
     * @return a string containing the names of all of the whiteboards saved on the server, each one separated
     *		   by a space, in the order that they were created
     *
     */
    protected synchronized String getAllWhiteboards() {
    	String allWhiteboards = "";
    	
    	for (int i = 0; i < names.size(); i++) {
    		allWhiteboards += names.get(i);
    		
    		// As long as the current name isn't the name of the last whiteboard
    		if(i != names.size() - 1) {
    			allWhiteboards += " ";
    		}
    	}
    	
    	return allWhiteboards;
    }
    
    /**
     * Attempts to open a whiteboard with the specified name, according to the specification of the 
     * 'open [username] [name]' command in the handleRequest method's specifications.
     * 
     * @param tokens cannot contain any empty strings, must be of length 3 or more and must have
     * 				 the following form:
     * 					open [username] [name 1] [name 2] [name 3] .... [name n]
     * 				 where [username] is the username of the client that is making this request, 
     * 				 where [name 1] [name 2] [name 2] [name 3] .... [name n] is the requested name of
     * 					   the whiteboard
     * @param notDebug false only when you are testing, true otherwise.
     * @return message to client
     */
    protected synchronized String openWhiteboard(final String[] tokens, final boolean notDebug) {
    	// The user has entered a whiteboard with a space, which isn't valid.
    	if (tokens.length != 3) {
    		return NOT_CREATED_ERROR;
    	}
    	
    	else {
    		String username = tokens[1];
    		String whiteboardName = tokens[2];
    		
    		int index = names.indexOf(whiteboardName);
    		
    		if (index == -1) {
    			return NOT_CREATED_ERROR;
    		}
    		else {
    			Whiteboard whiteboard = whiteboards.get(index);
    			
    			if (notDebug) {
    				String alsoEditing = whiteboard.addUsername(username);
    				sendMessageToSomeClients(whiteboard.getName(), "alsoediting " + alsoEditing);
    			}
    			
    			return "open " + whiteboardName + " " + whiteboard.toString();
    		}
    	}
    }
    
    /**
     * Attempts to assign a client a desired username, according to the specifications of the
     * 'create [username]' command in the handleRequest method's specifications.
     * 
     * @param tokens cannot contain any empty strings, must be of length 2 or more and must have
     * 				 the following form:
     * 					username [name 1] [name 2] [name 3] .... [name n]
     * 				 where [name 1] [name 2] [name 2] [name 3] .... [name n] is the requested username
     * @param socket the socket via which this client is connected to the server
     * @param notDebug false only when you are testing, true otherwise.
     * @return message back to the client
     * 
     */
    protected synchronized String createUsername(final String tokens[], final Socket socket, final boolean notDebug) {
    	if(tokens.length != 2) {
    		return USERNAME_CONTAINS_SPACE;
    	}
    	else {
    		String desiredUsername = tokens[1];
    		
    		// If the username has not been created yet...
    		if(! clients.containsKey(desiredUsername)) {
    			clients.put(desiredUsername, socket);
    			
    			// If you are not just testing this method
    			if (notDebug) {
    				sendMessageToAllClients("allUsersOnline " + getAllUsernames());
    			}
    			
    			return "usernameCreated " + desiredUsername;
    		}
    		else {
    			return USERNAME_ALREADY_CREATED;
    		}
    	}
    }
    
    /**
     * Attempts to log a client out of the server, according to the specifications of the
     * 'logout [username]' command in the handleRequest method's specifications.
     * 
     * @param tokens cannot contain any empty strings, must be of length 2 or more and must have
     * 				 the following form:
     * 					logout [username]
     * 				 where [username] is the username of the client that is to be disconnected from the
     * 					   server
     * @param notDebug false only when you are testing, true otherwise.
     * @return message back to the client
     */
    protected synchronized String logout (final String[] tokens, final boolean notDebug) {
    	if (tokens.length != 2) {
    		throw new RuntimeException ("The username that you requested to be logged out does not exist.");
    	}
    	
    	String username = tokens[1];
    	
    	// First removes the username from this server.
    	clients.remove(username);
    	
    	if (notDebug) {
    		sendMessageToAllClients("allUsersOnline " + getAllUsernames());
        	
        	// Next removes the username from all of the whiteboards that it was connected to.
        	for (Whiteboard whiteboard: whiteboards) {
        		if(whiteboard.hasUsername(username)) {
        			String usersOnWhiteboard = whiteboard.removeUsername(username);
        			sendMessageToSomeClients(whiteboard.getName(), "usersOnWhiteboard " + usersOnWhiteboard);
        		}
        	}
        	
    	}
    	
    	return LOGOUT_REQUEST;
    }
    
    /**
     * Attempts to disconnect a client from a whiteboard, according to the specifications of the
     * 'close [username] [whiteboard]' command in the handleRequest method's specifications.
     * 
     * @param tokens cannot contain any empty strings, must be of length exactly 3 and must have
     * 				 the following form:
     * 					close [username] [name]
     * 				 where [username] is the username of the client that is to be disconnected from the
     * 					   server
     * 				 where [name] is the name of the whiteboard
     * @return message back to the client
     */
    protected synchronized String closeWhiteboard (final String[] tokens) {
    	if (tokens.length != 3) {
    		throw new RuntimeException ("Invalid input.");
    	}

    	String username = tokens[1];

    	// Find the whiteboard that the client is currently connected to.
    	String whiteboardName = tokens[2];
    	int index = names.indexOf(whiteboardName);	
    	Whiteboard whiteboard = whiteboards.get(index);

    	// Remove the client from the whiteboard that it is currently connected to.
    	String usersOnWhiteboard = whiteboard.removeUsername(username);
    	sendMessageToSomeClients(whiteboard.getName(), "alsoediting " + usersOnWhiteboard);


    	return "";
    }
    
    /**
     * Returns the usernames of all of the clients connected to a specified whiteboard, 
     * according to the specifications of the 'allUsersOnWhiteboard [whiteboard]' command in the 
     * handleRequest method's specifications.
     * 
     * @param tokens cannot contain any empty strings, must be of length exactly 3 and must have
     * 				 the following form:
     * 					allUsersOnWhiteboard [whiteboard]
     * 				 where [whiteboard] is the name of a whiteboard on the server
     * @return message back to the client
     */
    private synchronized String getUsernamesOnWhiteboard(final String[] tokens) {
    	// Find the whiteboard that the client is currently connected to.
    	String whiteboardName = tokens[1];
    	int index = names.indexOf(whiteboardName);	
    	Whiteboard whiteboard = whiteboards.get(index);

    	return "alsoediting " + whiteboard.getUsernames();

    }
    
    /**
     * Attempts to draw a specified line segment on a specified whiteboard, according to the specifications of
     * the 'draw' command in the handleRequest method's specification.
     * 
     * @param tokens cannot contain any empty strings, must be of length exactly 10 and must have
     * 				 the following form:
     * 				 draw [name] [x1] [y1] [x2] [y2] [red] [green] [blue] [thickness]
     * 					where [name] is the name of a whiteboard on the server
     * 					where ([x1], [y1]) and ([x2], [y2]) are points on the whiteboard named [name]
     * 					where [x1], [y1], [x2], [y2], [red], [green], [blue] and [thickness] are the string 
     * 						  representations of nonnegative integers			  
     * @return message back to the server
     * 
     */
    private String drawWhiteboard (final String[] tokens) {
    	Whiteboard whiteboard;
    	String whiteboardName = tokens[1];
    	
    	synchronized (this) {
        	// First find the whiteboard.
        	int index = names.indexOf(whiteboardName);
        	whiteboard = whiteboards.get(index);
    	}

    	int x1 = Integer.parseInt(tokens[2]), y1 = Integer.parseInt(tokens[3]);
    	int x2 = Integer.parseInt(tokens[4]), y2 = Integer.parseInt(tokens[5]);
    	int red = Integer.parseInt(tokens[6]), green = Integer.parseInt(tokens[7]), blue = Integer.parseInt(tokens[8]);
    	int thickness = Integer.parseInt(tokens[9]);
    	
    	// Change the whiteboard and notifies relevant clients.
    	String messageBack = whiteboard.addLine(new Color(red, green, blue), x1, y1, x2, y2, thickness);
    	sendMessageToSomeClients(whiteboardName, "drawLine " + messageBack);
    	
    	return "";
    }
    
    /**
     * Attempts to clear a specified whiteboard, according to the specifications of
     * the 'reset' command in the handleRequest method's specification.
     * 
     * @param tokens cannot contain any empty strings, must be of length exactly 10 and must have
     * 				 the following form:
     * 				 reset [name] where [name] is the name of a whiteboard on the server	  
     * @return message back to the server
     * 
     */
    private synchronized String resetWhiteboard (final String[] tokens) {
    	// First find the whiteboard.
    	String whiteboardName = tokens[1];
    	int index = names.indexOf(whiteboardName);
    	Whiteboard whiteboard = whiteboards.get(index);
    	
    	whiteboard.clearWhiteboard();
    	
    	// Change the whiteboard and notifies relevant clients.
    	sendMessageToSomeClients(whiteboardName, "reset");
    	
    	// The above "sendMessageToSomeClients" will already send the clear message to the current client.
    	return "";
    }
    
    /**
     * Returns a string containing the usernames of all the clients that are online, each separated by a space
     * 
     * @return a string containing the usernames of all the clients that are online, each separated by a space
     */
    private synchronized String getAllUsernames() {
    	String allUsernames = "";
    	Set<String> usernames = clients.keySet();
    	
    	for (String username: usernames) {
    		allUsernames += username + " ";
    	}
    	
    	return allUsernames;
    }
    
    /**
     * Starts up the WhiteboardServer.
     * 
     */
	public static void main(final String[] args) {
		try {
			WhiteboardServer server = new WhiteboardServer(WhiteboardServer.port);
			server.serve();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
