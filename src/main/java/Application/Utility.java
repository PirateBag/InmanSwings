package Application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class Utility {
	static final Font titleFont = new Font("Arial", Font.PLAIN, 30); 
	static final Font labelFont = new Font("Arial", Font.PLAIN, 8);
    static final Border blackLine = BorderFactory.createLineBorder(Color.black);
    static final Font textFont = new Font("Arial", Font.PLAIN, 15);
	
	public static JLabel titleMaker(String xContent ) {
		var label = new JLabel( xContent  ); 
        label.setFont( titleFont );
        return label;
	}
	
	public static JLabel subTitleMaker( String xContent, int xPosition ) {
		var label = new JLabel( xContent, xPosition );
		label.setFont( textFont );
		return label;
	}
	
	
	public static JLabel labelMaker( String xContent, int xPosition ) {
		var label = new JLabel( xContent, xPosition );
		label.setFont( labelFont );
		return label;
	}
	
	public static JTextField createTextField( String xLabel ) {
		var rValue = new JTextField();
        var titledBorder = BorderFactory.createTitledBorder( blackLine, xLabel );
        ((TitledBorder) titledBorder).setTitleFont( labelFont );
        rValue.setFont( textFont );
        rValue.setMaximumSize( new Dimension( 400, 35) );
        rValue.setBorder( titledBorder );
        return rValue;
	}

	public static boolean isServiceUp() {
		Entrypoint.restTemplate = new RestTemplate();
		try {
			String rValue = Entrypoint.restTemplate.getForObject( "http://localhost:8080/status ", String.class );
			return true;
		} catch ( RestClientException e ) {
			System.out.println( "Application Service is down:  " + e.getMessage() );
		}
		return false;
	}
}
