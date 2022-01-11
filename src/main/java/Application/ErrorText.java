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

    public void signalError( String text ) {
        setText( text );
        setVisible( true );
    }
    public void clearError( ) {
        setText( "" );
        setVisible( false );
    }

}
