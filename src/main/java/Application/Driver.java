package Application;

import javax.swing.*;
import java.awt.*;

public class Driver {

	public void go() {
		//Create and set up the window.
		JFrame pane = new JFrame("Inman Swings");
		pane.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		if (!(pane.getLayout() instanceof BorderLayout)) {
			pane.add(new JLabel("Container doesn't use BorderLayout!"));
			return;
		}

		if (Constants.RIGHT_TO_LEFT) {
			pane.setComponentOrientation(
					java.awt.ComponentOrientation.RIGHT_TO_LEFT);
		}

		JButton button = new JButton("Welcome to Inman");
		pane.add(button, BorderLayout.PAGE_START);

		button = new JButton("MenuBar");
		button.setPreferredSize(new Dimension(100,600));  
		pane.add( button, BorderLayout.LINE_START );

		ScreenStateService.primaryPanel = pane;
		button = new JButton("Notifications" );
		button.setPreferredSize( new Dimension( 800,25 ));
		pane.add(button, BorderLayout.PAGE_END );
		ScreenStateService.setNotifications( button );
		ScreenStateService.refreshServer();
		
		ScreenStateService.evaluate( new Action( "empty", ScreenTransitionType.REPLACE, FormsLibrary.getEmpty() ) );
	
		if ( ScreenStateService.isServerConnected() ) {
			ScreenStateService.evaluate( new Action( "StartWithLogin", ScreenTransitionType.REPLACE, FormsLibrary.getLogin() ) );
		} else {
			ScreenStateService.evaluate( new Action( "StartWithConnect", ScreenTransitionType.REPLACE, FormsLibrary.getStartServer() ) );
		}
		pane.pack();
		pane.setVisible(true);
	}
}
