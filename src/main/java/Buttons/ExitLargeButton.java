package Buttons;

import javax.swing.*;

public class ExitLargeButton extends JButton {
    Icon icon = new ImageIcon( "gui/icons/logout_FILL0_wght400_GRAD0_opsz48.png" );
    public ExitLargeButton() {
        super();
        this.setIcon( icon );
        this.setDefaultCapable(false);
        this.setToolTipText("Exit Application");
    }
}
