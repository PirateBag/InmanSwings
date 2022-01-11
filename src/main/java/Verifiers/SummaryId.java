package Verifiers;

import java.awt.Component;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import Application.ScreenStateService;

public class SummaryId extends InputVerifier {
	@Override
	public boolean verify(JComponent input) {
		String text = ((JTextField) input).getText().trim();
		((JTextComponent) input).setText( text.toUpperCase() );

		JComponent parent = (JComponent) input.getParent();
		ErrorText errorText = null;
		for ( Component child : parent.getComponents() ) {
			if ( child instanceof ErrorText ) {
				if ( child != input ) {
					errorText = (ErrorText) child;
					break;
				}
			}
		}
		if ( null == errorText ) {
			throw new RuntimeException( "Unable to find error message field ");
		}

		if ( text.length() < 3 || text.length() > 10  ) {
			errorText.signalError( "SummaryId must be 3 to 10 characters long" );
			return false;
		} else {
			errorText.clearError();
		}
	
		ScreenStateService.getCurrentPanel().invalidate();
		ScreenStateService.getCurrentPanel().repaint();
		return true;
	}
}



