package Buttons;

import javax.swing.*;

public class EditLargeButton extends JButton {
    Icon icon = new ImageIcon( "gui/icons/edit_FILL0_wght400_GRAD0_opsz48.png" );
    public EditLargeButton() {
        super();
        this.setIcon( icon );
        this.setDefaultCapable(false);
        this.setToolTipText("Edit properties");
    }
}
