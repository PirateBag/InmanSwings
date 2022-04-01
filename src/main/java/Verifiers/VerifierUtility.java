package Verifiers;

import Application.ErrorText;

import javax.swing.*;
import java.awt.*;

public class VerifierUtility {

	/**
	 * Look for an ErrorText field that is a sibling of xChild.
	 * There can only be one.
	 * @param xChild the field to be validated
	 * @return ErrorText sibling.
	 */
	public static ErrorText findErrorTextAmongPeers( JComponent xChild ) {
		JComponent parent = (JComponent) xChild.getParent();
		ErrorText errorText = null;
		for ( Component sibling : parent.getComponents() ) {
			if ( sibling instanceof ErrorText ) {
				errorText = (ErrorText) sibling;
				break;
				}
			}

		if ( null == errorText ) {
			throw new RuntimeException( "Unable to find error message field ");
		}
		return errorText;
	}
}



