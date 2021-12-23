package Application;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		
		button = new JButton("Status Button" );
		button.setPreferredSize( new Dimension( 800,40 ));
		pane.add(button, BorderLayout.PAGE_END );
		ScreenStateService.primaryPanel = pane;

		FormsLibrary formsLibrary = new FormsLibrary();

		ScreenStateService.refreshServer();
		
		ScreenStateService.evaluate( new Action( "empty", ScreenTransitionType.REPLACE, formsLibrary.getEmpty() ) );
	
		if ( ScreenStateService.isServerConnected() ) {
			ScreenStateService.evaluate( new Action( "StartWithLogin", ScreenTransitionType.REPLACE, formsLibrary.getLogin() ) );
		} else {
			ScreenStateService.evaluate( new Action( "StartWithConnect", ScreenTransitionType.REPLACE, formsLibrary.getStartServer() ) );
		}
		pane.pack();
		pane.setVisible(true);
	}
}
