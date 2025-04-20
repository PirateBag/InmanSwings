package Buttons;

import javax.swing.*;

public class Spacer extends JButton {
    private static ImageIcon scalableIcon = ButtonUtility.basicButtonScaler( "gui/icons/space_24dp_5F6368_FILL0_wght200_GRAD0_opsz24.png" );

    public Spacer() {
        super();
        this.setIcon( scalableIcon );
        this.setDefaultCapable(false);
        this.setEnabled( false );
        this.setToolTipText( "Inactive Empty Space );" );
    }
}
