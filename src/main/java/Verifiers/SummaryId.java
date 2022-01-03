package Verifiers;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

public class SummaryId extends InputVerifier {
	@Override
    public boolean verify(JComponent input) {
        String text = ((JTextField) input).getText().trim();
        ((JTextComponent) input).setText( text.toUpperCase() );
        
        if ( text.length() < 3 || text.length() > 10  ) {
        	return false;
        }
        
        return true;
     }
 }

	

