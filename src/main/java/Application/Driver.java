package Application;

import Forms.Empty;
import Forms.Login;
import Forms.StartServer;

import javax.swing.*;
import java.awt.*;

public class Driver {
	public static JTextArea MenuBar =  new JTextArea("MenuBar");

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

		MenuBar.setPreferredSize(new Dimension(150,600));
		pane.add( MenuBar, BorderLayout.LINE_START );
		ScreenStateService.primaryPanel = pane;
		JButton button = new JButton("Notifications" );
		button.setPreferredSize( new Dimension( 800,25 ));
		pane.add(button, BorderLayout.PAGE_END );
		ScreenStateService.setNotifications( button );
		ScreenStateService.refreshServer();
		
		ScreenStateService.evaluate( new NextAction( "empty", ScreenTransitionType.REPLACE, Empty.class ) );
	
		if ( ScreenStateService.isServerConnected() ) {
			ScreenStateService.evaluate( new NextAction( "StartWithLogin", ScreenTransitionType.REPLACE, Login.class ) );
		} else {
			ScreenStateService.evaluate( new NextAction( "StartWithConnect", ScreenTransitionType.REPLACE, StartServer.class  ) );
		}
		pane.pack();
		pane.setVisible(true);
	}
}
