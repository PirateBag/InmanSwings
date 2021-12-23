package Application;


import java.util.Optional;
import javax.swing.JPanel;


import java.awt.*;
import com.inman.model.User;

public class ScreenStateService {
	static boolean isServerConnected;
	static Optional<User> currentUser;
	static JPanel currentPanel;
	static Container primaryPanel;
	
	public static JPanel getCurrentPanel() {
		return currentPanel;
	}

	public static void setCurrentPanel(JPanel xCurrentPanel) {
		currentPanel = xCurrentPanel;
	}


	/** 
	 * Check to see if we able to connect to an Inman server.
	 */
	public static void refreshServer() {
			isServerConnected = Utility.isServiceUp();
	}
	
	public static boolean isServerConnected() {
		return isServerConnected;
	}

	public static Optional<User> getCurrentUser() {
		return currentUser;
	}

	public static void setCurrentUser( User xCurrentUser) {
		currentUser = Optional.of( xCurrentUser );
	}
	
	public static void replaceComponent( Container panel, JPanel xNew ) {
		if ( currentPanel != null ) {
			currentPanel.setVisible( false); 
			//  panel.remove( (java.awt.Component) currentPanel );
		}
		panel.add( xNew );
		currentPanel = xNew;
		currentPanel.setVisible( true );
	}
	
	public static void evaluate( Action xAction  ) {
		switch ( xAction.getScreenTransitionType() ) {
		case NO_CHANGE :
			break;
		case REPLACE :
			replaceComponent( primaryPanel, xAction.getNextPanel() );
			break;
		case PUSH:
			break;
		case POP: 
		default:
			break;
		}
		
		currentPanel.invalidate();
		currentPanel.repaint();

		primaryPanel.invalidate();
		primaryPanel.repaint();
	}
}
	

