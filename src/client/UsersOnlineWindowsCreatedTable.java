package client;

import javax.swing.table.AbstractTableModel;

/**
 * UsersOnlineWindowsCreatedTable represents the table in the ClientService GUI
 * that displays the users online in the left hand column and the windows open in the 
 * right hand column.
 *
 * Abstraction Function:
 * 		usersOnline    - contains the usernames of all of the clients that are currently connected
 * 					     to the server
 * 		windowsCreated - contains the names of all of the whiteboards that are saved on the server
 * 
 * Representation Invariants:
 * 		Mutable
 *
 */
public class UsersOnlineWindowsCreatedTable extends AbstractTableModel {
	private static final long serialVersionUID = -6083448689321001431L;
	private String[] usersOnline;
	private String[] windowsCreated;

	/**
	 * Creates an empty UsersOnlineWindowsCreatedTable object
	 * 
	 */
	public UsersOnlineWindowsCreatedTable() {
		usersOnline = new String[] {" "};
		windowsCreated = new String[] {" "};
	}
	
	@Override
	public int getRowCount() {
		return Math.max(usersOnline.length, windowsCreated.length);
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// The column of users online
		if (columnIndex == 0) {
			if (rowIndex >= usersOnline.length) {
				return "";
			}
			else {
				return usersOnline[rowIndex];
			}
		}
		
		// The column of windows created
		else {
			if (rowIndex >= windowsCreated.length) {
				return "";
			}
			else {
				return windowsCreated[rowIndex];
			}
		}
	}
	
    @Override
    public synchronized String getColumnName(int columnIndex) {
        switch (columnIndex){
            case 0:
                return "Users Online";
            case 1:
                return "Whiteboards Created";
        }
        return "";  
    }
    
    /**
     * Change the contents of the "Users Online" column.
     * 
     * @param usernames an array containing the usernames of all of the clients connected
     * 					to the server
     * @modifies changes the contents of the "Users Online" column to be usernames.
     */
    public void setUsersOnline(final String[] usernames) {
    	usersOnline = usernames;
    	fireTableDataChanged();
    }
    
    /**
     * Changes the contents of the "Whiteboards Created" column.
     * 
     * @param whiteboards an array containing the names of all of the whiteboards that have
     * 					  been created on the server.
     * @modifies changes the contents of the "Whiteboards Created" column.
     */
    public void setWhiteboardsCreated(final String[] whiteboards) {
    	windowsCreated = whiteboards;
    	fireTableDataChanged();
    }
}
