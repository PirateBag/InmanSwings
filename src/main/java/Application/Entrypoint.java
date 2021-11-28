package Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ConnectException;

import javax.swing.*;

@SpringBootApplication
public class Entrypoint { 
public static boolean RIGHT_TO_LEFT = false;
public static JPanel serverNotFound;
public static JPanel login;


public static void addComponentsToPane(Container pane) {
	login          = createLoginScreen( pane );
	serverNotFound = createStartServerScreen( pane );
	
    if (!(pane.getLayout() instanceof BorderLayout)) {
        pane.add(new JLabel("Container doesn't use BorderLayout!"));
        return;
    }
     
    if (RIGHT_TO_LEFT) {
        pane.setComponentOrientation(
                java.awt.ComponentOrientation.RIGHT_TO_LEFT);
    }
     
    JButton button = new JButton("Welcome to Inman");
    pane.add(button, BorderLayout.PAGE_START);
     
    button = new JButton("MenuBar");
    button.setPreferredSize(new Dimension(100,600));  
    pane.add( button, BorderLayout.LINE_START );

    if ( ScreenStateService.isServerConnected() ) {
		pane.add( login );
  	} else {
    	pane.add( serverNotFound );
	}

	
    button = new JButton("Status Button" );
    button.setPreferredSize( new Dimension( 800,40 ));
    pane.add(button, BorderLayout.PAGE_END );
	}

	private static JPanel createStartServerScreen( Container pane ) {
	    JPanel panel = new JPanel( );
	    panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
	    
	    panel.add( Utility.titleMaker( "Please Start the Inman Application Server" ), BorderLayout.PAGE_START );
        panel.add( Utility.labelMaker(" ", JLabel.TRAILING),
        		BorderLayout.LINE_START  );
        panel.add( Utility.subTitleMaker("Expecting server at " + Constants.IMAN_SERVER_REQUEST, JLabel.TRAILING),
        		BorderLayout.LINE_START  );
        var button = new JButton( "Retry");
        button.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ScreenStateService.refreshServer();
			    if ( ScreenStateService.isServerConnected() ) {
			    	try {
			    		pane.remove( serverNotFound );
			    	} catch ( NullPointerException npe ) {
			    		System.out.println( "server not found not a problem" ); 
			    	}
			    	
			    	Component loginScreenComponent = null;
			    	for ( Component component : pane.getComponents() ) {
			    		if ( component == login ) {
			    			loginScreenComponent = component;
			    			break;
			    		}
			    	}
			    	if ( loginScreenComponent == null ) {			    	
			    	   	pane.add( login );
			    	}

				pane.revalidate();
				pane.repaint( );
			    }
				
			}
        });
        panel.add(button);
        
        
	    return panel;
	}

	private static JPanel createLoginScreen(Container container) {
	    JPanel panel = new JPanel( );
	    panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );

	    panel.add( Utility.titleMaker( "Registration Form" ),
        		BorderLayout.PAGE_START );
	    //  panel.setBackground( Color.red );

	    JPanel innerBox = new JPanel();
	    innerBox.setLayout( new BoxLayout( innerBox, BoxLayout.Y_AXIS ) );

	    JTextField username = Utility.createTextField( "User" );
        innerBox.add( username );

        
        JTextField password = Utility.createTextField( "Password" );
        innerBox.add( password );

        innerBox.add( Utility.labelMaker(" ", JLabel.TRAILING),
        		BorderLayout.LINE_START  );
        panel.add(innerBox );

	    JPanel buttonPanel = new JPanel( );
	    buttonPanel.setLayout( new BoxLayout( buttonPanel, BoxLayout.X_AXIS ) );
        var button = new JButton( "Cancel");
        buttonPanel.add(button);
        button = new JButton( "Login");
        buttonPanel.add(button);
        //  buttonPanel.setBackground( Color.blue );
        
        innerBox.add( buttonPanel, BorderLayout.PAGE_END );
        
	    return panel;
	}


  

/**
 * Create the GUI and show it.  For thread safety,
 * this method should be invoked from the
 * event dispatch thread.
 */
private static void createAndShowGUI(boolean isServiceUp ) {
     
    //Create and set up the window.
    JFrame frame = new JFrame("Inman Swings");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //Set up the content pane.
    addComponentsToPane(frame.getContentPane());
    //Use the content pane's default BorderLayout. No need for
    //setLayout(new BorderLayout());
    //Display the window.
    frame.pack();
    frame.setVisible(true);
}
 

public static RestTemplate restTemplate;

public static void main(String[] args) {
    /* Use an appropriate Look and Feel */
    try {
        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
    } catch (UnsupportedLookAndFeelException ex) {
        ex.printStackTrace();
    } catch (IllegalAccessException ex) {
        ex.printStackTrace();
    } catch (InstantiationException ex) {
        ex.printStackTrace();
    } catch (ClassNotFoundException ex) {
        ex.printStackTrace();
    }
    /* Turn off metal's use bold fonts */
    UIManager.put("swing.boldMetal", Boolean.FALSE);
    
    ScreenStateService.refreshServer();
     
    
    //Schedule a job for the event dispatch thread:
    //creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            createAndShowGUI( Utility.isServiceUp() );
        }
    });
}
}
