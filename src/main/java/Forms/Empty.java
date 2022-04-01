package Forms;

import Application.InmanPanel;
import Application.ScreenStateService;
import Application.Utility;

import javax.swing.*;
import java.awt.*;

public class Empty extends InmanPanel {
        public Empty() {

        setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
        add( Utility.titleMaker( "This is an empty screen" ), BorderLayout.PAGE_START );
        ScreenStateService.primaryPanel.add( this );
    }
}
