package Application;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.inman.model.User;
import com.inman.model.rest.StatusResponse;
import com.inman.model.rest.VerifyCredentialsRequest;
import com.inman.model.rest.VerifyCredentialsResponse;

public class FormsLibrary {
	public static JPanel startServer;
	public static JPanel login;
	public static JPanel empty;
	public static JPanel itemQuery;
	public static JPanel itemPropertyPanel;
	
	public FormsLibrary( ) {
	}
	
	public JPanel getLogin() {
		if ( login != null ) {
			return login;
		}
		
		JPanel loginPanel = new JPanel( );
		loginPanel.setLayout( new BoxLayout( loginPanel, BoxLayout.Y_AXIS ) );

		loginPanel.add( Utility.titleMaker( "Login" ),
				BorderLayout.PAGE_START );

		JPanel innerBox = new JPanel();
		innerBox.setLayout( new BoxLayout( innerBox, BoxLayout.Y_AXIS ) );
		
		JLabel errorMessage = Utility.createErrorMessage( JLabel.TRAILING  ) ;
		innerBox.add( errorMessage );

		JTextField username = Utility.createTextField( "User" );
		username.setText( "fred" );
		innerBox.add( username );

		JTextField password = Utility.createTextField( "Password" );
		password.setText( "dilban");
		innerBox.add( password );

		innerBox.add( Utility.labelMaker(" ", JLabel.LEFT),
				BorderLayout.LINE_START  );
		loginPanel.add(innerBox );

		JPanel buttonPanel = new JPanel( );
		buttonPanel.setLayout( new BoxLayout( buttonPanel, BoxLayout.X_AXIS ) );
		var button = new JButton( "Login");
		buttonPanel.add(button);
		button.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				VerifyCredentialsRequest verifyCredentialsRequest = 
						new VerifyCredentialsRequest( username.getText(), password.getText() );
				
				VerifyCredentialsResponse verifyCredentialsResponse = Main.restTemplate.postForObject( "http://localhost:8080/" + VerifyCredentialsRequest.rootUrl, 
						verifyCredentialsRequest, VerifyCredentialsResponse.class );
				
				if ( verifyCredentialsResponse.getStatus().equals( StatusResponse.INMAN_OK ) ) {
					User user = new User(); 
					user.setUserName( username.getText() );
					ScreenStateService.evaluate(
							new Action( "itemQuery", ScreenTransitionType.REPLACE, ItemQueryForm.getItemQuery( ) ) );
				} else {
					Utility.setErrorMessage( errorMessage, verifyCredentialsResponse.getMessage() );					
				}
			}
		} );  

		
		innerBox.add( buttonPanel, BorderLayout.PAGE_END );
		
		ScreenStateService.primaryPanel.add( loginPanel );
		return loginPanel;
	}
	
	
	public JPanel getStartServer( ) {
		if ( startServer != null ) {
			return startServer;
		}

		JPanel startServerPanel = new JPanel( );
		startServerPanel.setLayout( new BoxLayout( startServerPanel, BoxLayout.Y_AXIS ) );
		startServerPanel.add( Utility.titleMaker( "Please Start the Inman Application Server" ), BorderLayout.PAGE_START );
		startServerPanel.add( Utility.labelMaker(" ", JLabel.TRAILING),
				BorderLayout.LINE_START  );
		startServerPanel.add( Utility.subTitleMaker("Expecting server at " + Constants.IMAN_SERVER_REQUEST, JLabel.TRAILING),
				BorderLayout.LINE_START  );
		var button = new JButton( "Retry");

		button.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ScreenStateService.refreshServer();
				if ( ScreenStateService.isServerConnected() ) {
					ScreenStateService.evaluate(
							new Action( "login", ScreenTransitionType.REPLACE, getLogin() ) );
				}

			}
		} );  
		startServerPanel.add(button);

		startServer = startServerPanel;
		ScreenStateService.primaryPanel.add( startServer );
		return startServer; 
	}
	
	
	public JPanel getEmpty( ) {
		if ( empty != null ) {
			return empty;
		}

		empty = new JPanel( );
		empty.setLayout( new BoxLayout( empty, BoxLayout.Y_AXIS ) );
		empty.add( Utility.titleMaker( "This is an empty screen" ), BorderLayout.PAGE_START );
		ScreenStateService.primaryPanel.add( empty );
		return empty; 
	}

}
