package Buttons;

import javax.swing.*;

public class DoneButton extends JButton {
    Icon icon = new ImageIcon( "gui/icons/done_FILL0_wght400_GRAD0_opsz24.png" );
    public DoneButton() {
        super();
        this.setIcon( icon );
        this.setDefaultCapable(false);
        this.setToolTipText("Exit Application");
    }
}
