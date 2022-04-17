package Forms;

import Application.InmanPanel;
import Application.ScreenStateService;
import Application.Utility;

import javax.swing.*;
import java.awt.*;

public class Error extends InmanPanel {
        public Error( Exception e ) {

        setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
        add( Utility.titleMaker( "You have come to an error.  Please check the logs.  " ), BorderLayout.PAGE_START );
        add( Utility.createTextField( e.getMessage() ) );
        ScreenStateService.primaryPanel.add( this );
    }
}
