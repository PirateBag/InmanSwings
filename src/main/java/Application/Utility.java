package Application;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Utility {
	static final Font titleFont = new Font("Arial", Font.PLAIN, 30); 
	static final Font labelFont = new Font("Arial", Font.PLAIN, 8);
    public static final Border blackLine = BorderFactory.createLineBorder(Color.black);
    static final Font textFont = new Font("Arial", Font.PLAIN, 12);
	
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
	
	public static JTextField createErrorField( String xLabel ) {
		var rValue = new JTextField();
        rValue.setFont( textFont );
        rValue.setMaximumSize( new Dimension( 400, 23) );
        rValue.setForeground( Color.RED );
        return rValue;
	}
	
	
	
	public static JLabel createErrorMessage( int xPosition ) {
		var label = new JLabel( "" );
		label.setFont( textFont );
		label.setVisible( false );
		label.setMinimumSize(new Dimension( 400, 35));
        label.setMaximumSize( new Dimension( 400, 35) );
		return label;
	}
	
	public static void setErrorMessage( JLabel label, String xText ) {
		if ( xText.length() == 0 ) {
			label.setText( "" );
			label.setVisible( false );
		} else {
			label.setText( xText );
			label.setVisible( true );			
		}
	}

	public static boolean isServiceUp() {
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getForObject( "http://localhost:8080/status ", String.class );
			return true;
		} catch ( RestClientException e ) {
			System.out.println( "Application Service is down:  " + e.getMessage() );
		} catch ( Exception e ) {
			System.out.println( "A more spectaular error occurred while checking server status:  " + e.getMessage() );
		}
		return false;
	}

	//Move to Common
	private static List<HttpMessageConverter<?>> getMessageConverters() {
		List<HttpMessageConverter<?>> converters =
				new ArrayList<HttpMessageConverter<?>>();
		converters.add(new MappingJackson2HttpMessageConverter());
		return converters;
	}

	public static RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setMessageConverters( getMessageConverters() );
		return restTemplate;
	}

	public static boolean isEmptyOrNull(StringBuilder xString) {
		return xString.length() == 0;
	}

	/**
	 * Trim the lasst character from the input string.
	 * No bad input checking.
	 *
	 * @param oneCharacterTooMany
	 * @return allButTheLastCharacter;
	 */
    public static String removeLastChar(String oneCharacterTooMany ) {
		return oneCharacterTooMany.substring(0, oneCharacterTooMany.length()-1 );
    }
}
