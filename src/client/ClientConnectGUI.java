package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.*;

import server.WhiteboardServer;

/**
 * ClientConnectGUI is a datatype that represents the GUI of requesting to connect to the server
 * as a client.
 * 
 * Abstraction Function:
 * 		None
 * 
 * Representation Invariant:
 * 		Once this object is able to connect to the server, the ClientConnectGUI closes itself and makes
 * 		a WhiteboardClient object that is connected to the server via the socket.
 * 
 * Thread safety argument:
 * 		Threads in this system:
 * 			- GUI thread
 * 			- one thread per requested IP address
 * 
 * 		This class is thread safe by confinement.
 * 
 * 		Once an IP address has been requested by the client, the GUI thread hands this requested IP address over
 * 		to another thread and then looses its own reference to that IP address.  So each requested IP address is 
 * 		confined to the thread dealing with that requested IP address.
 * 
 * 		The GUI is confined to the GUI thread.  When a thread dealing with a requested IP address wishes to 
 * 		change the GUI, it sends a message to the GUI thread, stating so.
 * 
 */
public class ClientConnectGUI extends JFrame {
	private static final long serialVersionUID = -7794837287037418099L;
	
	private static final int WIDTH = 400;
	private static final int HEIGHT = 200;
	private static final String ERROR_MESSAGE = "Could not connect to server.  Please try again.";
	
	private final JLabel prompt;
	private final JTextField ipAddress;
	private final JButton connectButton;
	private JLabel errorMessage;
	
	/**
	 * Creates and opens a ClientConnectGUI object.
	 * 
	 */
	public ClientConnectGUI() {
		setTitle("Connect to the Whiteboard Server");
		setSize(WIDTH, HEIGHT);
		setLocation(10, 200);
		
		prompt = new JLabel("Please input the IP address of the server:");
		ipAddress = new JTextField(30);
		
		connectButton = new JButton("Connect!");
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
	 * The first row of the window should contain a label prompting the user to enter their IP
	 * address and a text field where they can enter their IP address.
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
				.addComponent(prompt)
				.addComponent(ipAddress)
				);
		promptLayout.setVerticalGroup(promptLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(prompt)
				.addComponent(ipAddress)
				);

		return promptPanel;
	}
	
	/**
	 * Creates the second row of the window.
	 * The second row of the window should contain a button that the user can press
	 * to attempt to connect to the server.
	 * 
	 * @return the container representing the second row of the window.
	 */
	private JPanel createSecondRow() {
		JPanel connectButtonPanel = new JPanel();
		GroupLayout connectButtonLayout = new GroupLayout(connectButtonPanel);
		connectButtonPanel.setLayout(connectButtonLayout);

		connectButtonLayout.setAutoCreateGaps(true);
		connectButtonLayout.setAutoCreateContainerGaps(true);

		connectButtonLayout.setHorizontalGroup(connectButtonLayout.createSequentialGroup()
				.addComponent(connectButton)
				);
		
		connectButtonLayout.setVerticalGroup(connectButtonLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(connectButton)
				);

		return connectButtonPanel;
	}
	
	/**
	 * Adds an ActionListener so that whenever an action is performed on the
	 * connectButton, the program attempts to connect to the server.
	 * 
	 */
	private void addConnectToServerActionListener() {
		ActionListener actionPerformed = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final String inputtedAddress = ipAddress.getText();
				ipAddress.setText("");
				
				Thread thread = new Thread (new Runnable() {

					@Override
					public void run() {
						
						// If it is able to connect,...
						try {
							new WhiteboardClient(new Socket (inputtedAddress, WhiteboardServer.port));
							dispose();
						}
						
						// If it is not able to connect,...
						catch (IOException e){
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									errorMessage.setText(ERROR_MESSAGE);
								}
							});
						}
					}
					
				});
				thread.start();
			}
		};
		
		connectButton.addActionListener(actionPerformed);	
		ipAddress.addActionListener(actionPerformed);
	}
}
