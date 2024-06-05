package Buttons;

import javax.swing.*;

public class AddButton extends JButton {
    Icon addIcon = new ImageIcon( "gui/icons/add_FILL0_wght400_GRAD0_opsz24.png" );
    public AddButton() {
        super();
        this.setIcon( addIcon );
        this.setToolTipText("Add");
    }
}
