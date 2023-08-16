package Buttons;

import javax.swing.*;

public class SaveLargeButton extends JButton {
    Icon icon = new ImageIcon( "gui/icons/save_as_FILL0_wght400_GRAD0_opsz48.png" );
    public SaveLargeButton() {
        super();
        this.setIcon( icon );
        this.setDefaultCapable(false);
        this.setToolTipText("Save changes");
    }
}
