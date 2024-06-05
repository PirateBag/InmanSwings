package Buttons;

import javax.swing.*;

public class ClearButton extends JButton {
    Icon icon = new ImageIcon( "gui/icons/undo_FILL0_wght400_GRAD0_opsz24.png" );

    public ClearButton() {
        super();
        this.setIcon( icon );
        this.setDefaultCapable(false);
        this.setToolTipText("Clear");
    }

    public ClearButton( String toolTipText ) {
        super();
        this.setIcon( icon );
        this.setDefaultCapable(false);
        this.setToolTipText( toolTipText);
    }
}
