package Buttons;

import javax.swing.*;

public class EditButton extends JButton {
    Icon icon = new ImageIcon( "gui/icons/edit_square_FILL0_wght400_GRAD0_opsz24.png" );
    public EditButton() {
        super();
        this.setIcon( icon );
        this.setDefaultCapable(false);
        this.setToolTipText("Edit properties");
    }
}
