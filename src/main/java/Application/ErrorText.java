package Application;
import javax.swing.*;
import java.awt.*;

public class ErrorText extends JTextArea {

    private static final long serialVersionUID = 1L;
    public ErrorText() {
        var titledBorder = BorderFactory.createTitledBorder( Utility.blackLine );
        setText( "" );
        setForeground( Color.RED );
        setFont( Utility.textFont );
        setMaximumSize( new Dimension( 400,70 ) );
        setBorder( titledBorder );
        setLineWrap(true);
    }

    /**
     * Sets the error text on input.  Tolerates null and empty strings.
     * @param text
     * @return true if an error was signalled.
     */
    public boolean signalError( String text ) {
        if ( text == null || text.length() > 0 ) {
            setText(text);
            setVisible(true);
            return true;
        }
        return false;
    }
    public void clearError( ) {
        setText( "" );
        setVisible( false );
    }

    public boolean hasError() {
        return getText().length() > 0;
    }

    //  Make checking for noError clearer.
    public boolean hasNoError() { return !hasError(); };


}
