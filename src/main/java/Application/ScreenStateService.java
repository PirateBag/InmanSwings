package Application;


import com.inman.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.Optional;
import java.util.Stack;

public class ScreenStateService {
	static boolean isServerConnected;
	static Optional<User> currentUser;
	static InmanPanel currentPanel;
	public static Container primaryPanel;
	static Stack<InmanPanel> jPanelStack = new Stack<>();
	static Stack<NextAction> actionHistory = new Stack<>();
	static Logger logger = LoggerFactory.getLogger( ScreenStateService.class );

	static JButton notifications;

	public static void setNotifications(JButton notifications) {
		ScreenStateService.notifications = notifications;
	}
	public JButton getNotifications( ) {
		return notifications;
	}

	public static JPanel getCurrentPanel() {
		return currentPanel;
	}

	public static void setCurrentPanel(JPanel xCurrentPanel) {
		currentPanel = (InmanPanel) xCurrentPanel;
	}

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
		}
		panel.add( xNew );
		currentPanel = (InmanPanel) xNew;
		currentPanel.setVisible( true );
	}

	public static String getStack() {
		if (jPanelStack.isEmpty()) {
			return "The stack is empty";
		}
		StringBuilder rValue = new StringBuilder( );
		Iterator<InmanPanel> inmanPanelIterator = jPanelStack.elements().asIterator();
		while ( inmanPanelIterator.hasNext() ) {
			rValue.append("   " + inmanPanelIterator.next().getClass().getName().replace( "Forms.", "" ) + "\n");
		}
        return rValue.toString();
	}
	
	public static void evaluate( NextAction xAction  ) {

		switch ( xAction.getScreenTransitionType() ) {
		case REPLACE :
			actionHistory.push( xAction );
			replaceComponent(primaryPanel, xAction.getNextPanel());
			break;
		case PUSH:
			actionHistory.push( xAction );
			jPanelStack.push( currentPanel );
			replaceComponent( primaryPanel, xAction.getNextPanel() );
			currentPanel.updateStateWhenOpeningNewChild( xAction );
			notifications.setText( xAction.getActionName() );
			break;
		case POP:
			actionHistory.push( xAction );
			InmanPanel nextPanel = (InmanPanel) jPanelStack.peek();
			nextPanel.updateStateWhenChildCloses( xAction );
			nextPanel.invalidate();
			replaceComponent( primaryPanel, jPanelStack.pop() );
			notifications.setText( xAction.getActionName() );
			break;
		default:
			break;
		}
		//  logger.info( getStack() );
		Driver.MenuBar.setText( getStack() );

		currentPanel.invalidate();
		currentPanel.repaint();

		primaryPanel.invalidate();
		primaryPanel.repaint();
	}
}
	

