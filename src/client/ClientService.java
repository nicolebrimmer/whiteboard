package client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

/**
 * ClientService represents the window by which a client that has been assigned an username can
 * request to open and create whiteboards and can see the usernames of other clients that are 
 * currently connected to the server.
 * 
 * This will update as new users are logged in and logged out as well as new boards are created.
 * Since boards are always active while the server is on whiteboards cannot be deleted.
 * 
 * There is also a JTextArea to send messages to the user in case they do something that is not 
 * allowed or need to be notified of a error occuring.
 * 
 * 
 * 
 */
public class ClientService extends JFrame {
	private static final long serialVersionUID = 7094651500977042330L;
	
	private JTextField windowName;
	private JLabel windowLabel;
	private JTable openWindows;
	private JTextArea errorMsg;
	private JButton createBoard;
	private JButton openBoard;
	private JScrollPane scrollPane;
	public WhiteboardClient client;
	public UsersOnlineWindowsCreatedTable table;

	public ClientService (WhiteboardClient client) {
		this.client = client;

		setTitle("Hello " + this.client.getUsername() + "!");
		setSize(400, 300);
		setLocation(300, 400);
		
		windowName = new JTextField();
		
		windowLabel = new JLabel();
		windowLabel.setText("Board Name:");
		
		createBoard = new JButton();
		createBoard.setText("Create Board");
		
		openBoard = new JButton();
		openBoard.setText("Open Board");
		
		table = new UsersOnlineWindowsCreatedTable();
		openWindows = new JTable(table);
		
		scrollPane = new JScrollPane(openWindows, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(400, 150));
		
		errorMsg = new JTextArea();
		
		// Initialize the layout of the container panel.
		JPanel container = new JPanel();
		GroupLayout layout = new GroupLayout(container);
		container.setLayout(layout);

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		JPanel connectButtonPanel = createSecondRow();
		
		// Puts the rows together.
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addComponent(windowLabel)
				.addComponent(windowName)
				.addComponent(connectButtonPanel)
				.addComponent(scrollPane)
				.addComponent(errorMsg)
				);
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(windowLabel)
				.addComponent(windowName)
				.addComponent(connectButtonPanel)
				.addComponent(scrollPane)
				.addComponent(errorMsg)
				);
		
		add(container);
		
		// Add action listeners.
		addActionListenerForCreate();
		addActionListenerForOpen();
		addActionListenerOperationOnClose();
	}
		
	
	/**
	 * Creates the second row of the window.
	 * The second row of the window should contain two buttons, one of which is for creating whiteboards
	 * the other one of which is for opening whiteboards.
	 * 
	 * @return the container representing the first row of the window.
	 */
	private JPanel createSecondRow() {
		JPanel buttonPanel = new JPanel();
		GroupLayout buttonLayout = new GroupLayout(buttonPanel);
		buttonPanel.setLayout(buttonLayout);

		buttonLayout.setAutoCreateGaps(true);
		buttonLayout.setAutoCreateContainerGaps(true);

		buttonLayout.setHorizontalGroup(buttonLayout.createSequentialGroup()
				.addComponent(createBoard)
				.addComponent(openBoard)
				);
		buttonLayout.setVerticalGroup(buttonLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				.addComponent(createBoard)
				.addComponent(openBoard)
				);

		return buttonPanel;
	}
	
	/**
	 * Changes the table of the users online.
	 * 
	 * @param usersOnLine an array containing the usernames of all of the users online.
	 */
	public void setUsersOnLine (final String[] usersOnLine) {
		table.setUsersOnline(usersOnLine);
	}
	
	/**
	 * Changes the table of the whiteboards saved on the server.
	 * 
	 * @param whiteboards an array containing the names of all of the whiteboards saved on
	 * 					  the server.
	 */
	public void setWhiteboardsCreated (final String[] whiteboards) {
		table.setWhiteboardsCreated(whiteboards);
	}
	
    /**
     * Changes the error message in the GUI window.
     * 
     * @param errorMessage the error message that the server sent back to this client.
     * @modifies changes the contents of the error message portion of this window.
     */
    public void setErrorMessage(final String errorMessage) {
    	errorMsg.setText(errorMessage);
    }
    
    /**
     * Adds an ActionListener to the create button in order to create whiteboards.
     * 
     */
    public void addActionListenerForCreate() {
    	ActionListener createButton = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				errorMsg.setText("");
				client.sendMessage("create " + windowName.getText());
			}
    		
    	};
    	
    	createBoard.addActionListener(createButton);
    }
    
    /**
     * Adds an ActionListener to the open button in order to open whiteboards.
     * 
     */
    public void addActionListenerForOpen() {
    	ActionListener openButton = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (client.hasWhiteboardOpen()) {
					windowName.setText("");
					errorMsg.setText("You can only open one whiteboard at a time.");
				}
				else {
					errorMsg.setText("");
					client.sendMessage("open " + client.getUsername() + " " + windowName.getText());
					windowName.setText("");
				}
			}
    		
    	};
    	
    	openBoard.addActionListener(openButton);
    }
    
    /**
     * Adds an ActionListener to the closing of the ClientService window so that the client
     * is logged out of the server.
     * 
     */
    public void addActionListenerOperationOnClose() {
    	WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                client.sendMessage("logout " + client.getUsername());
            }
        };
        addWindowListener(exitListener);
    }
}
