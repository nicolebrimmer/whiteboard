package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


/**
 * ClientUsername is a datatype that represents the GUI of a request of a username from the client.
 * 
 * Abstraction Function:
 * 		client: the WhiteboardClient from which this GUI is requesting a username
 * 
 * Representation Invariant:
 * 		None
 * 
 * Thread safety Argument:
 * 		The threads in the system are:
 * 			- GUI thread
 * 			- one thread per requested username
 * 		
 * 		This class is thread safe by confinement.	
 * 
 * 		Once a username has been requested by the client, the GUI thread hands this requested username over
 * 		to another thread and then looses its own reference to that username.  So each requested username is 
 * 		confined to the thread dealing with that requested username.
 * 
 * 		The GUI is confined to the GUI thread.  When a thread dealing with a requested username wishes to 
 * 		change the GUI, it sends a message to the GUI thread, stating so.
 *
 */
public class ClientUsername extends JFrame {
	private static final long serialVersionUID = -7794837287037418099L;
	
	private static final int WIDTH = 400;
	private static final int HEIGHT = 200;
	
	private final JLabel usernamePrompt;
	private final JTextField username;
	private final JButton createUsernameButton;
	private JLabel errorMessage;
	
	private final WhiteboardClient client;
	
	/**
	 * Creates and opens a ClientUsername object.
	 * 
	 * @param client the client that this ClientUsername object is serving.
	 * 
	 */
	public ClientUsername(WhiteboardClient client) {
		setTitle("Connect to the Whiteboard Server");
		setSize(WIDTH, HEIGHT);
		setLocation(10, 200);
		
		this.client = client;
		
		usernamePrompt = new JLabel("Please input your desired username: ");
		username = new JTextField(30);
		
		createUsernameButton = new JButton("Create username!");
		errorMessage = new JLabel("");
		
		// Initialize the layout of the container panel.
		JPanel container = new JPanel();
		GroupLayout layout = new GroupLayout(container);
		container.setLayout(layout);

		// Creates the rows of the window.
		JPanel promptPanel = createFirstRow();
		JPanel connectButtonPanel = createSecondRow();

		// Puts the rows together.
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addComponent(promptPanel)
				.addComponent(connectButtonPanel)
				.addComponent(errorMessage)
				);
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(promptPanel)
				.addComponent(connectButtonPanel)
				.addComponent(errorMessage)
				);
		add(container);
		
		addConnectToServerActionListener();
	}
	
	/**
	 * Creates the first row of the window.
	 * The first row of the window should contain a label prompting the user to enter their desired
	 * username and a text field where they can enter their desired username.
	 * 
	 * @return the container representing the first row of the window.
	 */
	private JPanel createFirstRow() {
		JPanel promptPanel = new JPanel();
		GroupLayout promptLayout = new GroupLayout(promptPanel);
		promptPanel.setLayout(promptLayout);

		promptLayout.setAutoCreateGaps(true);
		promptLayout.setAutoCreateContainerGaps(true);

		promptLayout.setHorizontalGroup(promptLayout.createSequentialGroup()
				.addComponent(usernamePrompt)
				.addComponent(username)
				);
		promptLayout.setVerticalGroup(promptLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(usernamePrompt)
				.addComponent(username)
				);

		return promptPanel;
	}
	
	/**
	 * Creates the second row of the window.
	 * The second row of the window should contain a button that the user can press
	 * to attempt to create their desired username.
	 * 
	 * @return the container representing the second row of the window.
	 */
	private JPanel createSecondRow() {
		JPanel createUsernameButtonPanel = new JPanel();
		GroupLayout connectButtonLayout = new GroupLayout(createUsernameButtonPanel);
		createUsernameButtonPanel.setLayout(connectButtonLayout);

		connectButtonLayout.setAutoCreateGaps(true);
		connectButtonLayout.setAutoCreateContainerGaps(true);

		connectButtonLayout.setHorizontalGroup(connectButtonLayout.createSequentialGroup()
				.addComponent(createUsernameButton)
				);
		
		connectButtonLayout.setVerticalGroup(connectButtonLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(createUsernameButton)
				);

		return createUsernameButtonPanel;
	}
	
	/**
	 * Adds an ActionListener so that whenever an action is performed on the
	 * createUsernameButton button or the createUsername text field, 
	 * a request is sent to the server to associate this client with the desired
	 * username.
	 * 
	 */
	private void addConnectToServerActionListener() {
		ActionListener actionPerformed = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final String inputtedUsername = username.getText();
				username.setText("");
				
				// Create a thread to deal with every requested username.
				Thread thread = new Thread (new Runnable() {

					@Override
					public void run() {
						client.sendMessage("username " + inputtedUsername);
					}
				});
				thread.start();
			}
			
		};
		
		createUsernameButton.addActionListener(actionPerformed);	
		username.addActionListener(actionPerformed);
	}
	
	/**
	 * Displays the server's returned error message in this GUI.
	 * 
	 * @param errorMessageFromServer the error message from the server detailing why the username that
	 * 					   			 the client requested has been rejected
	 * @modify the GUI to display the error message
	 * 
	 */
	public void updateErrorMessage(final String errorMessageFromServer) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				errorMessage.setText(errorMessageFromServer);
			}
		});
	}
}
